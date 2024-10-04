package com.holparb.moviefinder.movies.data.repository

import androidx.paging.Pager
import com.holparb.moviefinder.movies.data.dao.MovieDao
import com.holparb.moviefinder.movies.data.datasource.remote.TmdbApi
import com.holparb.moviefinder.movies.data.entity.MovieEntity
import com.holparb.moviefinder.movies.domain.model.MovieListItem
import com.holparb.moviefinder.movies.domain.repository.MovieRepository
import com.holparb.moviefinder.movies.domain.util.MovieError
import com.holparb.moviefinder.movies.domain.util.Resource
import com.holparb.moviefinder.testutils.JsonReader
import io.mockk.clearAllMocks
import io.mockk.mockk
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
class MovieRepositoryTest {
    private lateinit var tmdbApi: TmdbApi
    private lateinit var server: MockWebServer
    private lateinit var movieDao: MovieDao
    private lateinit var repository: MovieRepository
    private lateinit var mockPager: Pager<Int, MovieEntity>

    @Before
    fun setup() {
        server = MockWebServer()
        tmdbApi = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(MoshiConverterFactory.create())
            .build().create(TmdbApi::class.java)
        movieDao = mockk()
        mockPager = mockk()
        repository = MovieRepositoryImpl(
            api = tmdbApi,
            movieDao = movieDao,
            popularMoviesPager = mockPager,
            topRatedMoviesPager = mockPager,
            upcomingMoviesPager = mockPager
        )
    }

    @After
    fun tearDown() {
        server.shutdown()
        clearAllMocks()
    }

    @Test
    fun getPopularMovies_returns_movies_successfully() = runBlocking {
        val mockResponse = MockResponse().addHeader("Content-Type", "application/json; charset=utf-8")
            .setResponseCode(200).setBody(JsonReader.readJsonFile("MovieListResponse.json"))
        server.enqueue(mockResponse)

        val result: Resource<List<MovieListItem>, MovieError.NetworkError> = repository.getPopularMovies()

        Assert.assertTrue(result is Resource.Success)
    }
}