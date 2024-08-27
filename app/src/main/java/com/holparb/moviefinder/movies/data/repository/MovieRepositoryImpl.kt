package com.holparb.moviefinder.movies.data.repository

import android.util.Log
import com.holparb.moviefinder.movies.data.dao.MovieDao
import com.holparb.moviefinder.movies.data.datasource.remote.TmdbApi
import com.holparb.moviefinder.movies.data.dto.MovieListDto
import com.holparb.moviefinder.movies.data.mappers.toMovieEntity
import com.holparb.moviefinder.movies.data.mappers.toMovieListItem
import com.holparb.moviefinder.movies.domain.model.MovieListItem
import com.holparb.moviefinder.movies.domain.repository.MovieRepository
import com.holparb.moviefinder.movies.domain.util.MovieError
import com.holparb.moviefinder.movies.domain.util.Resource
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor (
    private val api: TmdbApi,
    private val dao: MovieDao
): MovieRepository {

    private suspend fun getMovies(
        page: Int,
        region: String,
        apiFunction: suspend TmdbApi.(page: Int, region: String) -> MovieListDto
    ): Resource<List<MovieListItem>, MovieError.NetworkError> {
        return try {
            Resource.Success(
                api.apiFunction(page, region).results.map { movieListItemDto ->
                    dao.upsertMovie(movieListItemDto.toMovieEntity())
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