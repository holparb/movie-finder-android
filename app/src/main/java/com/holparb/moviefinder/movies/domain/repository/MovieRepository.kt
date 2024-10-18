package com.holparb.moviefinder.movies.domain.repository

import androidx.paging.PagingData
import com.holparb.moviefinder.movies.domain.model.MovieListItem
import com.holparb.moviefinder.movies.domain.util.MovieError
import com.holparb.moviefinder.core.utils.Resource
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getPopularMovies(page: Int = 1, region: String = "US"): Resource<List<MovieListItem>, MovieError>
    suspend fun getTopRatedMovies(page: Int = 1, region: String = "US"): Resource<List<MovieListItem>, MovieError>
    suspend fun getUpcomingMovies(page: Int = 1, region: String = "US"): Resource<List<MovieListItem>, MovieError>
    suspend fun getPopularMoviesWithPagination(): Flow<PagingData<MovieListItem>>
    suspend fun getTopRatedMoviesWithPagination(): Flow<PagingData<MovieListItem>>
    suspend fun getUpcomingWithPagination(): Flow<PagingData<MovieListItem>>
}