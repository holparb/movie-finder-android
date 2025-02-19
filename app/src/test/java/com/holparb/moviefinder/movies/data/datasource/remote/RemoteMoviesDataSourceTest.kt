package com.holparb.moviefinder.movies.data.datasource.remote

import com.holparb.moviefinder.core.domain.util.Result
import com.holparb.moviefinder.core.domain.util.errors.NetworkError
import com.holparb.moviefinder.movies.domain.model.MovieListType
import com.holparb.moviefinder.testutils.JsonReader
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@RunWith(JUnit4::class)
class RemoteMoviesDataSourceTest {

    private var isSuccess: Boolean? = null
        get() = field ?: throw IllegalStateException("Mock has not beet initialized")

    private fun givenSuccess() {
        isSuccess = true
    }

    private fun givenFailure() {
        isSuccess = false
    }

    private lateinit var client: HttpClient
    private lateinit var mockEngine: MockEngine
    private lateinit var remoteMoviesDataSource: RemoteMoviesDataSource

    @Before
    fun setup() {
        mockEngine = MockEngine { request ->
            val statusCode = if(isSuccess == true) HttpStatusCode.OK else HttpStatusCode.InternalServerError
            val content = if(isSuccess == true) JsonReader.readJsonFile("MovieListResponse.json") else ""
            respond(
                content = content,
                status = statusCode,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        client = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(
                    json = Json {
                        ignoreUnknownKeys = true
                    }
                )
            }
        }

        remoteMoviesDataSource = RemoteMoviesDataSource(client)
    }

    @Test
    fun data_source_returns_movie_list_successfully() = runBlocking {
        givenSuccess()
        val response = remoteMoviesDataSource.getMoviesList(movieListType = MovieListType.PopularMovies)
        Assert.assertTrue(response is Result.Success)
        Assert.assertEquals(20, (response as Result.Success).data.size)
    }

    @Test
    fun get_movie_list_data_source_fails_with_server_error() = runBlocking {
        givenFailure()
        val response = remoteMoviesDataSource.getMoviesList(movieListType = MovieListType.PopularMovies)
        Assert.assertTrue(response is Result.Error)
        Assert.assertEquals(NetworkError.SERVER_ERROR, (response as Result.Error).error)
    }

    @Test
    fun get_watchlist_is_successful() = runBlocking {
        givenSuccess()
        val response = remoteMoviesDataSource.getWatchlist("session_id")
        Assert.assertTrue(response is Result.Success)
        Assert.assertEquals(20, (response as Result.Success).data.size)
    }

    @Test
    fun get_watchlist_fails_with_server_error() = runBlocking {
        givenFailure()
        val response = remoteMoviesDataSource.getWatchlist("session_id")
        Assert.assertTrue(response is Result.Error)
        Assert.assertEquals(NetworkError.SERVER_ERROR, (response as Result.Error).error)
    }
}