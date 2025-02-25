package com.holparb.moviefinder.movies.data.datasource.remote

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.holparb.moviefinder.core.domain.util.Result
import com.holparb.moviefinder.movies.data.datasource.local.MovieDatabase
import com.holparb.moviefinder.movies.data.entity.MovieEntity
import com.holparb.moviefinder.movies.data.entity.RemoteKeyEntity
import com.holparb.moviefinder.movies.data.mappers.toMovieEntity
import com.holparb.moviefinder.movies.domain.model.MovieListType
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
class MovieRemoteMediator(
    private val movieDatabase: MovieDatabase,
    private val remoteMoviesDataSource: RemoteMoviesDataSource,
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
            val movieListResponse = when (movieListType) {
                MovieListType.PopularMovies -> remoteMoviesDataSource.getMoviesList(movieListType = MovieListType.PopularMovies, page = page)
                MovieListType.TopRatedMovies -> remoteMoviesDataSource.getMoviesList(movieListType = MovieListType.TopRatedMovies, page = page)
                MovieListType.UpcomingMovies -> remoteMoviesDataSource.getMoviesList(movieListType = MovieListType.UpcomingMovies, page = page)
            }

            when(movieListResponse) {
                is Result.Error -> MediatorResult.Error(Exception("Movie data could not be fetched"))
                is Result.Success -> {
                    val nextPage = if (movieListResponse.data.isNotEmpty()) page + 1 else null

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
                        val movieEntities = movieListResponse.data.map { movieListItemDto ->
                            val entity = movieDatabase.movieDao.getMovieById(movieListItemDto.id) ?: movieListItemDto.toMovieEntity()
                            when(movieListType) {
                                MovieListType.PopularMovies -> entity.copy(isPopular = true)
                                MovieListType.TopRatedMovies -> entity.copy(isTopRated = true)
                                MovieListType.UpcomingMovies -> entity.copy(isUpcoming = true)
                            }
                        }
                        movieDatabase.movieDao.upsertMovies(movieEntities)
                    }

                    MediatorResult.Success(endOfPaginationReached = movieListResponse.data.isEmpty())
                }
            }

        } catch (e: Exception) {
            Log.e(MovieRemoteMediator::class.simpleName, e.message.toString())
            MediatorResult.Error(e)
        }
    }
}