package com.holparb.moviefinder.movies.domain.repository

import androidx.paging.PagingData
import com.holparb.moviefinder.core.domain.util.NetworkError
import com.holparb.moviefinder.core.domain.util.Result
import com.holparb.moviefinder.movies.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getPopularMovies(page: Int = 1, region: String = "US"): Result<List<Movie>, NetworkError>
    suspend fun getTopRatedMovies(page: Int = 1, region: String = "US"): Result<List<Movie>, NetworkError>
    suspend fun getUpcomingMovies(page: Int = 1, region: String = "US"): Result<List<Movie>, NetworkError>
    suspend fun getPopularMoviesWithPagination(): Flow<PagingData<Movie>>
    suspend fun getTopRatedMoviesWithPagination(): Flow<PagingData<Movie>>
    suspend fun getUpcomingWithPagination(): Flow<PagingData<Movie>>
}