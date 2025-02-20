package com.holparb.moviefinder.movies.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.holparb.moviefinder.movies.data.entity.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Upsert
    suspend fun upsertMovies(movies: List<MovieEntity>)

    @Upsert
    suspend fun upsertMovie(movie: MovieEntity)

    @Query("DELETE FROM movies")
    suspend fun clearAll()

    @Query("SELECT * FROM movies WHERE is_popular ORDER BY release_date DESC LIMIT 20")
    fun getPopularMoviesForHomeScreen(): Flow<List<MovieEntity>>

    @Query("SELECT * FROM movies WHERE is_popular ORDER BY popularity DESC")
    fun getPopularMoviesWithPagination():PagingSource<Int, MovieEntity>

    @Query("SELECT * FROM movies WHERE is_top_rated ORDER BY rating DESC")
    fun getTopRatedMoviesWithPagination(): PagingSource<Int, MovieEntity>

    @Query("SELECT * FROM movies WHERE is_upcoming")
    fun getUpcomingMoviesWithPagination(): PagingSource<Int, MovieEntity>

    @Query("SELECT * FROM movies WHERE is_watchlist")
    fun getWatchlist(): Flow<List<MovieEntity>>
}