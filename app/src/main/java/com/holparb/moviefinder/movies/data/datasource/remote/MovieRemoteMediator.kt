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
import com.holparb.moviefinder.movies.data.entity.RemoteKeyType
import com.holparb.moviefinder.movies.data.mappers.toMovieEntity
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
class MovieRemoteMediator(
    private val movieDatabase: MovieDatabase,
    private val tmdbApi: TmdbApi,
    private val remoteKeyType: RemoteKeyType
): RemoteMediator<Int, MovieEntity>() {

    override suspend fun initialize(): InitializeAction {
        val remoteKey = movieDatabase.remoteKeyDao.getRemoteKeyById(remoteKeyType.ordinal) ?: return InitializeAction.LAUNCH_INITIAL_REFRESH

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
            val remoteKey = movieDatabase.remoteKeyDao.getRemoteKeyById(remoteKeyType.ordinal)
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
            val movieListResponse = when (remoteKeyType) {
                RemoteKeyType.PopularMovies -> tmdbApi.getPopularMovies(page = page)
                RemoteKeyType.TopRatedMovies -> tmdbApi.getPopularMovies(page = page)
                RemoteKeyType.UpcomingMovies -> tmdbApi.getPopularMovies(page = page)
            }

            val nextPage = if (movieListResponse.results.isNotEmpty()) page + 1 else null

            movieDatabase.withTransaction {
                if(loadType == LoadType.REFRESH) {
                    movieDatabase.movieDao.clearAll()
                }
                movieDatabase.remoteKeyDao.upsertRemoteKey(
                    RemoteKeyEntity(
                        id = remoteKeyType.ordinal,
                        nextPage = nextPage,
                        lastUpdated = System.currentTimeMillis()
                    )
                )
                val movieEntities = movieListResponse.results.map {
                    it.toMovieEntity()
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