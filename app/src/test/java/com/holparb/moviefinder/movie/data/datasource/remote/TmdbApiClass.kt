package com.holparb.moviefinder.movie.data.datasource.remote

import com.holparb.moviefinder.movies.data.datasource.remote.TmdbApi
import com.holparb.moviefinder.testutils.JsonReader
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@RunWith(JUnit4::class)
class TmdbApiClass {
    private lateinit var api: TmdbApi
    private lateinit var server: MockWebServer

    @Before
    fun before() {
        server = MockWebServer()
        api = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(MoshiConverterFactory.create())
            .build().create(TmdbApi::class.java)
    }

    @After
    fun after() {
        server.shutdown()
    }

    @Test
    fun movies_list_fetched_correctly(): Unit = runBlocking {
        val mockResponse = MockResponse().addHeader("Content-Type", "application/json; charset=utf-8")
        mockResponse.setResponseCode(200).setBody(JsonReader.readJsonFile("MovieListResponse.json"))
        server.enqueue(mockResponse)

        val result = api.getPopularMovies()

        Assert.assertNotNull(result)
        Assert.assertEquals(20, result.results.size)
    }
}