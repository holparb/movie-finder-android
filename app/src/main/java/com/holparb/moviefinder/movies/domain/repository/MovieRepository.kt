package com.holparb.moviefinder.movies.domain.repository

import com.holparb.moviefinder.movies.domain.model.MovieListItem
import com.holparb.moviefinder.movies.domain.util.MovieError
import com.holparb.moviefinder.movies.domain.util.Resource

interface MovieRepository {
    suspend fun getPopularMovies(page: Int = 1, region: String = "US"): Resource<List<MovieListItem>, MovieError.NetworkError>
    suspend fun getTopRatedMovies(page: Int = 1, region: String = "US"): Resource<List<MovieListItem>, MovieError.NetworkError>
    suspend fun getUpcomingMovies(page: Int = 1, region: String = "US"): Resource<List<MovieListItem>, MovieError.NetworkError>
}