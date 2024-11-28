package com.holparb.moviefinder.movies.data.repository

import android.util.Log
import androidx.paging.Pager
import com.holparb.moviefinder.core.data.networking.HttpClientFactory
import com.holparb.moviefinder.core.domain.util.NetworkError
import com.holparb.moviefinder.core.domain.util.Result
import com.holparb.moviefinder.movies.data.dao.MovieDao
import com.holparb.moviefinder.movies.data.datasource.remote.RemoteMoviesDataSource
import com.holparb.moviefinder.movies.data.entity.MovieEntity
import com.holparb.moviefinder.movies.domain.repository.MovieRepository
import com.holparb.moviefinder.testutils.JsonReader
import io.ktor.client.engine.cio.CIO
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class MovieRepositoryTest {
    private lateinit var dataSource: RemoteMoviesDataSource
    private lateinit var server: MockWebServer
    private lateinit var movieDao: MovieDao
    private lateinit var repository: MovieRepository
    private lateinit var mockPager: Pager<Int, MovieEntity>

    @Before
    fun setup() {
        server = MockWebServer()
        dataSource = RemoteMoviesDataSource(HttpClientFactory.create(CIO.create()))
        movieDao = mockk()
        mockPager = mockk()
        repository = MovieRepositoryImpl(
            moviesDataSource = dataSource,
            movieDao = movieDao,
            popularMoviesPager = mockPager,
            topRatedMoviesPager = mockPager,
            upcomingMoviesPager = mockPager
        )
        mockkStatic(Log::class)
        every { Log.e(any(), any<String>()) } returns 0
    }

    @After
    fun tearDown() {
        server.shutdown()
        clearAllMocks()
    }

    @Test
    fun getPopularMovies_returns_movies_successfully(): Unit = runBlocking {
        val mockResponse = MockResponse().addHeader("Content-Type", "application/json; charset=utf-8")
            .setResponseCode(200).setBody(JsonReader.readJsonFile("MovieListResponse.json"))
        server.enqueue(mockResponse)
        coEvery { movieDao.upsertMovie(any<MovieEntity>()) } returns Unit

        val result = repository.getPopularMovies()

        Assert.assertTrue(result is Result.Success)
        Assert.assertEquals(20, (result as Result.Success).data.size)
    }

    @Test
    fun getPopularMovies_returns_fails_because_of_http_exception(): Unit = runBlocking {
        val mockResponse = MockResponse().addHeader("Content-Type", "application/json; charset=utf-8")
            .setResponseCode(400).setBody("Invalid")
        server.enqueue(mockResponse)
        coEvery { movieDao.upsertMovie(any<MovieEntity>()) } returns Unit

        val result = repository.getPopularMovies()

        Assert.assertTrue(result is Result.Error)
        Assert.assertTrue((result as Result.Error).error is NetworkError)
    }

    @Test
    fun getPopularMovies_returns_fails_because_of_database_exception(): Unit = runBlocking {
        val mockResponse = MockResponse().addHeader("Content-Type", "application/json; charset=utf-8")
            .setResponseCode(200).setBody(JsonReader.readJsonFile("MovieListResponse.json"))
        server.enqueue(mockResponse)
        coEvery { movieDao.upsertMovie(any<MovieEntity>()) } throws Exception()

        val result = repository.getPopularMovies()

        Assert.assertTrue(result is Result.Error)
        //Assert.assertTrue((result as Result.Error).error is MovieError.LocalDatabaseError)
    }
}