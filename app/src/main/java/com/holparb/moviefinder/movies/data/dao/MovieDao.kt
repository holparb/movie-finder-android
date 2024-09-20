package com.holparb.moviefinder.movies.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.holparb.moviefinder.movies.data.entity.MovieEntity

@Dao
interface MovieDao {
    @Upsert
    suspend fun upsertMovies(movies: List<MovieEntity>)

    @Upsert
    suspend fun upsertMovie(movie: MovieEntity)

    @Query("DELETE FROM movies")
    suspend fun clearAll()

    @Query("SELECT * FROM movies WHERE is_popular")
    fun getPopularMoviesWithPagination(): PagingSource<Int, MovieEntity>

    @Query("SELECT * FROM movies WHERE is_top_rated ORDER BY rating DESC")
    fun getTopRatedMoviesWithPagination(): PagingSource<Int, MovieEntity>

    @Query("SELECT * FROM movies WHERE is_upcoming")
    fun getUpcomingMoviesWithPagination(): PagingSource<Int, MovieEntity>
}