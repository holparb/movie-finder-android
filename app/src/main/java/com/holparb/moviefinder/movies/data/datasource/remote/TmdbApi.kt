package com.holparb.moviefinder.movies.data.datasource.remote

import com.holparb.moviefinder.BuildConfig
import com.holparb.moviefinder.movies.data.dto.MovieListResponseDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface TmdbApi {
    @GET("3/movie/popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int = 1,
        @Query("region") region: String = "US",
        @Header("Authorization") authHeader: String = "Bearer ${BuildConfig.API_ACCESS_TOKEN}"
    ): MovieListResponseDto

    @GET("3/movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("page") page: Int = 1,
        @Query("region") region: String = "US",
        @Header("Authorization") authHeader: String = "Bearer ${BuildConfig.API_ACCESS_TOKEN}"
    ): MovieListResponseDto

    @GET("3/movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("page") page: Int = 1,
        @Query("region") region: String = "US",
        @Header("Authorization") authHeader: String = "Bearer ${BuildConfig.API_ACCESS_TOKEN}"
    ): MovieListResponseDto

    companion object {
        const val BASE_URL: String = "https://api.themoviedb.org/"
        const val IMAGE_URL_W500: String = "https://image.tmdb.org/t/p/w500"
        const val IMAGE_URL_W780: String = "https://image.tmdb.org/t/p/w780"
    }
}