package com.holparb.moviefinder.movies.data.datasource.remote

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.holparb.moviefinder.movies.data.datasource.local.MovieDatabase
import com.holparb.moviefinder.movies.data.entity.MovieEntity
import com.holparb.moviefinder.movies.data.mappers.toMovieEntity

@OptIn(ExperimentalPagingApi::class)
class MovieRemoteMediator(
    private val movieDatabase: MovieDatabase,
    private val tmdbApi: TmdbApi,
    private val tmdpApiCallType: TmdbApi.Companion.ApiCallType
): RemoteMediator<Int, MovieEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>
    ): MediatorResult {
        return try {
            val currentPage = when(loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    if(state.lastItemOrNull()  == null) {
                        1
                    } else {
                        state.pages.size / state.config.pageSize + 1
                    }
                }
            }
            Log.d(MovieRemoteMediator::class.simpleName, "Current page: $currentPage")
            val movieListResponse = when (tmdpApiCallType) {
                TmdbApi.Companion.ApiCallType.GET_POPULAR_MOVIES -> tmdbApi.getPopularMovies(page = currentPage)
                TmdbApi.Companion.ApiCallType.GET_TOP_RATED_MOVIES -> tmdbApi.getTopRatedMovies(page = currentPage)
                TmdbApi.Companion.ApiCallType.GET_UPCOMING_MOVIES -> tmdbApi.getUpcomingMovies(page = currentPage)
            }

            movieDatabase.withTransaction {
                if(loadType == LoadType.REFRESH) {
                    movieDatabase.movieDao.clearAll()
                }
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