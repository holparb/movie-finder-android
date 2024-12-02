package com.holparb.moviefinder.movies.data.datasource.remote

import com.holparb.moviefinder.BuildConfig
import com.holparb.moviefinder.core.data.networking.constructUrl
import com.holparb.moviefinder.core.data.networking.safeCall
import com.holparb.moviefinder.core.domain.util.NetworkError
import com.holparb.moviefinder.core.domain.util.Result
import com.holparb.moviefinder.core.domain.util.map
import com.holparb.moviefinder.movies.data.dto.MovieListItemDto
import com.holparb.moviefinder.movies.data.dto.MovieListResponseDto
import com.holparb.moviefinder.movies.domain.model.MovieListType
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.HttpHeaders
import io.ktor.http.headers
import javax.inject.Inject

class RemoteMoviesDataSource @Inject constructor(
    private val httpClient: HttpClient
) {
    suspend fun getMoviesList(
        movieListType: MovieListType,
        page: Int = 1,
        region: String = "US",
        authHeader: String = "Bearer ${BuildConfig.API_ACCESS_TOKEN}"
    ): Result<List<MovieListItemDto>, NetworkError> {
        val urlString = when(movieListType) {
            MovieListType.PopularMovies -> constructUrl("/movie/popular")
            MovieListType.TopRatedMovies -> constructUrl("/movie/top_rated")
            MovieListType.UpcomingMovies -> constructUrl("/movie/upcoming")
        }
        return safeCall<MovieListResponseDto> {
            httpClient.get(urlString = urlString) {
                parameter("page", page)
                parameter("region", region)
                headers {
                    append(HttpHeaders.Authorization, authHeader)
                }
            }
        }.map {
            it.results
        }
    }

    companion object {
        const val IMAGE_URL_W500: String = "https://image.tmdb.org/t/p/w500"
        const val IMAGE_URL_W780: String = "https://image.tmdb.org/t/p/w780"
    }
}