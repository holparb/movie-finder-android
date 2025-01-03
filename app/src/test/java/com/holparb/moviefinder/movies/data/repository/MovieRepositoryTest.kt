package com.holparb.moviefinder.movies.data.repository

import android.util.Log
import androidx.paging.Pager
import com.holparb.moviefinder.core.domain.util.errors.DatabaseError
import com.holparb.moviefinder.core.domain.util.errors.NetworkError
import com.holparb.moviefinder.core.domain.util.Result
import com.holparb.moviefinder.movies.data.dao.MovieDao
import com.holparb.moviefinder.movies.data.datasource.remote.RemoteMoviesDataSource
import com.holparb.moviefinder.movies.data.dto.MovieListItemDto
import com.holparb.moviefinder.movies.data.entity.MovieEntity
import com.holparb.moviefinder.movies.domain.model.MovieListType
import com.holparb.moviefinder.movies.domain.repository.MovieRepository
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class MovieRepositoryTest {
    private lateinit var dataSource: RemoteMoviesDataSource
    private lateinit var movieDao: MovieDao
    private lateinit var repository: MovieRepository
    private lateinit var mockPager: Pager<Int, MovieEntity>

    private val movieList = (1 .. 20).map { index ->
        MovieListItemDto(id = index, title = "title", overview = "Some text here")
    }

    @Before
    fun setup() {
        dataSource = mockk()
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
        clearAllMocks()
    }

    @Test
    fun getPopularMovies_returns_movies_successfully(): Unit = runBlocking {
        coEvery { dataSource.getMoviesList(movieListType = MovieListType.PopularMovies) } returns Result.Success(movieList)
        coEvery { movieDao.upsertMovie(any<MovieEntity>()) } returns Unit

        val result = repository.getPopularMovies()

        Assert.assertTrue(result is Result.Success)
        Assert.assertEquals(20, (result as Result.Success).data.size)
    }

    @Test
    fun getPopularMovies_returns_fails_because_of_http_exception(): Unit = runBlocking {
        coEvery { dataSource.getMoviesList(movieListType = MovieListType.PopularMovies) } returns Result.Error(
            NetworkError.SERVER_ERROR)
        coEvery { movieDao.upsertMovie(any<MovieEntity>()) } returns Unit

        val result = repository.getPopularMovies()

        Assert.assertTrue(result is Result.Error)
        Assert.assertTrue((result as Result.Error).error == NetworkError.SERVER_ERROR)
    }

    @Test
    fun getPopularMovies_returns_fails_because_of_database_exception(): Unit = runBlocking {
        coEvery { dataSource.getMoviesList(movieListType = MovieListType.PopularMovies) } returns Result.Success(movieList)
        coEvery { movieDao.upsertMovie(any<MovieEntity>()) } throws Exception()

        val result = repository.getPopularMovies()

        Assert.assertTrue(result is Result.Error)
        Assert.assertTrue((result as Result.Error).error == DatabaseError.UPSERT_ERROR)
    }
}