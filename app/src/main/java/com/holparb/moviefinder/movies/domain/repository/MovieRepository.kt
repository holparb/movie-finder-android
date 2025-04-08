package com.holparb.moviefinder.movies.domain.repository

import androidx.paging.PagingData
import com.holparb.moviefinder.core.domain.util.Result
import com.holparb.moviefinder.core.domain.util.errors.DataError
import com.holparb.moviefinder.movies.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getPopularMovies(page: Int = 1, region: String = "US"): Result<List<Movie>, DataError.Network>
    suspend fun getTopRatedMovies(page: Int = 1, region: String = "US"): Result<List<Movie>, DataError.Network>
    suspend fun getUpcomingMovies(page: Int = 1, region: String = "US"): Result<List<Movie>, DataError.Network>
    suspend fun getPopularMoviesWithPagination(): Flow<PagingData<Movie>>
    suspend fun getTopRatedMoviesWithPagination(): Flow<PagingData<Movie>>
    suspend fun getUpcomingWithPagination(): Flow<PagingData<Movie>>
    suspend fun getMovieDetails(movieId: Int): Result<Movie, DataError>
    suspend fun getWatchlist(sessionId: String, page: Int = 1): Result<List<Movie>, DataError>
    suspend fun updateWatchlistState(movieId: Int, isWatchlist: Boolean): Result<Unit, DataError.Database>
    suspend fun search(query: String, page: Int = 1): Result<List<Movie>, DataError.Network>
}