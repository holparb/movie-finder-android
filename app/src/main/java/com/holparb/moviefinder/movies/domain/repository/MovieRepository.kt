package com.holparb.moviefinder.movies.domain.repository

import androidx.paging.PagingData
import com.holparb.moviefinder.movies.domain.model.MovieListItem
import com.holparb.moviefinder.movies.domain.util.MovieError
import com.holparb.moviefinder.movies.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getPopularMovies(page: Int = 1, region: String = "US"): Resource<List<MovieListItem>, MovieError.NetworkError>
    suspend fun getTopRatedMovies(page: Int = 1, region: String = "US"): Resource<List<MovieListItem>, MovieError.NetworkError>
    suspend fun getUpcomingMovies(page: Int = 1, region: String = "US"): Resource<List<MovieListItem>, MovieError.NetworkError>
    suspend fun getPopularMoviesWithPagination(): Flow<PagingData<MovieListItem>>
    suspend fun getTopRatedMoviesWithPagination(): Flow<PagingData<MovieListItem>>
    suspend fun getUpcomingWithPagination(): Flow<PagingData<MovieListItem>>
}