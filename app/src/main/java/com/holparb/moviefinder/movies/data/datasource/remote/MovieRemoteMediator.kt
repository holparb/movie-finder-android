package com.holparb.moviefinder.movies.data.datasource.remote

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.holparb.moviefinder.movies.data.datasource.local.MovieDatabase
import com.holparb.moviefinder.movies.data.entity.MovieEntity
import com.holparb.moviefinder.movies.data.entity.RemoteKeyEntity
import com.holparb.moviefinder.movies.data.mappers.toMovieEntity
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
class MovieRemoteMediator(
    private val movieDatabase: MovieDatabase,
    private val tmdbApi: TmdbApi,
    private val movieListType: MovieListType
): RemoteMediator<Int, MovieEntity>() {

    override suspend fun initialize(): InitializeAction {
        val remoteKey = movieDatabase.remoteKeyDao.getRemoteKeyById(movieListType.ordinal) ?: return InitializeAction.LAUNCH_INITIAL_REFRESH

        val cacheTimeout = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)

        return if (remoteKey.lastUpdated == null || System.currentTimeMillis() - remoteKey.lastUpdated <= cacheTimeout)
        {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>
    ): MediatorResult {
        return try {
             val remoteKey = movieDatabase.remoteKeyDao.getRemoteKeyById(movieListType.ordinal)
            val page = when(loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    if(state.lastItemOrNull()  == null) {
                        1
                    } else {
                        remoteKey?.nextPage ?: return MediatorResult.Success(endOfPaginationReached = true)
                    }
                }
            }
            Log.d(MovieRemoteMediator::class.simpleName, "Current page: $page")
            val movieListResponse = when (movieListType) {
                MovieListType.PopularMovies -> tmdbApi.getPopularMovies(page = page)
                MovieListType.TopRatedMovies -> tmdbApi.getTopRatedMovies(page = page)
                MovieListType.UpcomingMovies -> tmdbApi.getUpcomingMovies(page = page)
            }

            val nextPage = if (movieListResponse.results.isNotEmpty()) page + 1 else null

            movieDatabase.withTransaction {
                if(loadType == LoadType.REFRESH) {
                    movieDatabase.movieDao.clearAll()
                }
                movieDatabase.remoteKeyDao.upsertRemoteKey(
                    RemoteKeyEntity(
                        id = movieListType.ordinal,
                        nextPage = nextPage,
                        lastUpdated = System.currentTimeMillis()
                    )
                )
                val movieEntities = movieListResponse.results.map {
                    when(movieListType) {
                        MovieListType.PopularMovies -> it.toMovieEntity(isPopular = true)
                        MovieListType.TopRatedMovies -> it.toMovieEntity(isTopRated = true)
                        MovieListType.UpcomingMovies -> it.toMovieEntity(isUpcoming = true)
                    }
                }
                movieDatabase.movieDao.upsertMovies(movieEntities)
            }

            MediatorResult.Success(endOfPaginationReached = movieListResponse.results.isEmpty())
        } catch (e: Exception) {
            Log.e(MovieRemoteMediator::class.simpleName, e.message.toString())
            MediatorResult.Error(e)
        }
    }
}