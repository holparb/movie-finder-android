package com.holparb.moviefinder.movies.data.repository

import android.util.Log
import com.holparb.moviefinder.BuildConfig
import com.holparb.moviefinder.movies.data.datasource.remote.TmdbApi
import com.holparb.moviefinder.movies.data.mappers.toMovieListItem
import com.holparb.moviefinder.movies.domain.model.MovieListItem
import com.holparb.moviefinder.movies.domain.repository.MovieRepository
import com.holparb.moviefinder.movies.domain.util.MovieError
import com.holparb.moviefinder.movies.domain.util.Resource
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor (private val api: TmdbApi): MovieRepository {
    override suspend fun getPopularMovies(page: Int, region: String): Resource<List<MovieListItem>, MovieError.NetworkError> {
        return try {
            Resource.Success(
                api.getPopularMovies(authHeader = "Bearer ${BuildConfig.API_ACCESS_TOKEN}").results.map { movieListItemDto ->
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
}