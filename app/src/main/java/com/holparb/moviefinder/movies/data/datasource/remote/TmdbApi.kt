package com.holparb.moviefinder.movies.data.datasource.remote

import com.holparb.moviefinder.movies.data.dto.MovieListDto
import retrofit2.http.GET
import retrofit2.http.Query

interface TmdbApi {
    @GET("3/movie/popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int = 1,
        @Query("region") region: String = "US"
    ): MovieListDto

    @GET("3/movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("page") page: Int = 1,
        @Query("region") region: String = "US"
    ): MovieListDto

    @GET("3/movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("page") page: Int = 1,
        @Query("region") region: String = "US"
    ): MovieListDto

    companion object {
        val BASE_URL: String = "https://api.themoviedb.org/"
        val IMAGE_URL: String = "https://image.tmdb.org/t/p/w500"
    }
}