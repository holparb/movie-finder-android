package com.holparb.moviefinder.movies.data.repository

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.holparb.moviefinder.movies.data.datasource.local.MovieDatabase
import com.holparb.moviefinder.movies.data.datasource.remote.MovieRemoteMediator
import com.holparb.moviefinder.movies.data.datasource.remote.TmdbApi
import com.holparb.moviefinder.movies.data.dto.MovieListResponseDto
import com.holparb.moviefinder.movies.data.entity.MovieEntity
import com.holparb.moviefinder.movies.data.mappers.toMovieEntity
import com.holparb.moviefinder.movies.data.mappers.toMovieListItem
import com.holparb.moviefinder.movies.domain.model.MovieListItem
import com.holparb.moviefinder.movies.domain.repository.MovieRepository
import com.holparb.moviefinder.movies.domain.util.MovieError
import com.holparb.moviefinder.movies.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor (
    private val api: TmdbApi,
    private val database: MovieDatabase,
): MovieRepository {

    private suspend fun getMovies(
        page: Int,
        region: String,
        apiFunction: suspend TmdbApi.(page: Int, region: String) -> MovieListResponseDto
    ): Resource<List<MovieListItem>, MovieError.NetworkError> {
        return try {
            Resource.Success(
                api.apiFunction(page, region).results.map { movieListItemDto ->
                    database.movieDao.upsertMovie(movieListItemDto.toMovieEntity())
                    movieListItemDto.toMovieListItem()
                }
            )
        }
        catch(e: Exception) {
            Log.e(this::class.simpleName, e.localizedMessage ?: "Unknown error")
            Resource.Error(
                error = MovieError.NetworkError("Couldn't fetch movie data, please try again!")
            )
        }
    }

    override suspend fun getPopularMovies(
        page: Int,
        region: String
    ): Resource<List<MovieListItem>, MovieError.NetworkError> {
        return getMovies(
            apiFunction = { _, _ -> api.getPopularMovies(page, region) },
            page = page,
            region = region
        )
    }

    @OptIn(ExperimentalPagingApi::class)
    override suspend fun getPopularMoviesWithPagination(): Flow<PagingData<MovieEntity>> {
        return Pager(
            PagingConfig(pageSize = 20),
            remoteMediator = MovieRemoteMediator(
                movieDatabase = database,
                tmdbApi = api,
                tmdpApiCallType = TmdbApi.Companion.ApiCallType.GET_POPULAR_MOVIES
            ),
            pagingSourceFactory = { database.movieDao.getPopularMoviesWithPagination() }
        ).flow
    }

    override suspend fun getTopRatedMovies(
        page: Int,
        region: String
    ): Resource<List<MovieListItem>, MovieError.NetworkError> {
        return getMovies(
            apiFunction = { _, _ -> api.getTopRatedMovies(page, region) },
            page = page,
            region = region
        )
    }

    override suspend fun getUpcomingMovies(
        page: Int,
        region: String
    ): Resource<List<MovieListItem>, MovieError.NetworkError> {
        return getMovies(
            apiFunction = { _, _ -> api.getUpcomingMovies(page, region) },
            page = page,
            region = region
        )
    }
}