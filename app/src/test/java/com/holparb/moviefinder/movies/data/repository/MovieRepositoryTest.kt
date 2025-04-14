package com.holparb.moviefinder.movies.data.repository

import android.util.Log
import androidx.paging.Pager
import com.holparb.moviefinder.core.domain.util.Result
import com.holparb.moviefinder.core.domain.util.errors.DataError
import com.holparb.moviefinder.core.domain.util.errors.DatabaseError
import com.holparb.moviefinder.core.domain.util.errors.NetworkError
import com.holparb.moviefinder.movies.data.dao.MovieDao
import com.holparb.moviefinder.movies.data.datasource.remote.RemoteMoviesDataSource
import com.holparb.moviefinder.movies.data.dto.MovieListItemDto
import com.holparb.moviefinder.movies.data.entity.MovieEntity
import com.holparb.moviefinder.movies.data.mappers.toMovie
import com.holparb.moviefinder.movies.data.mappers.toMovieEntity
import com.holparb.moviefinder.movies.domain.model.MovieListType
import com.holparb.moviefinder.movies.domain.repository.MovieRepository
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
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

    private val movieListDtos = (1 .. 20).map { index ->
        MovieListItemDto(id = index, title = "title", overview = "Some text here")
    }

    private val movieListEntities = movieListDtos.map { it.toMovieEntity() }

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
        coEvery { dataSource.getMoviesList(movieListType = MovieListType.PopularMovies) } returns Result.Success(movieListDtos)
        coEvery { movieDao.getMovieById(any<Int>()) } returns movieListEntities.first()
        coEvery { movieDao.upsertMovies(any<List<MovieEntity>>()) } returns Unit

        val result = repository.getPopularMovies()

        Assert.assertTrue(result is Result.Success)
        Assert.assertEquals(movieListDtos.map { it.toMovie() }, (result as Result.Success).data)
    }

    @Test
    fun getPopularMovies_returns_fails_because_of_http_exception(): Unit = runBlocking {
        coEvery { dataSource.getMoviesList(movieListType = MovieListType.PopularMovies) } returns Result.Error(
            NetworkError.SERVER_ERROR)
        coEvery { movieDao.upsertMovie(any<MovieEntity>()) } returns Unit

        val result = repository.getPopularMovies()

        Assert.assertTrue(result is Result.Error)
        Assert.assertTrue((result as Result.Error).error == DataError.Network(NetworkError.SERVER_ERROR))
    }

    @Test
    fun getPopularMovies_returns_fails_because_of_database_exception(): Unit = runBlocking {
        coEvery { dataSource.getMoviesList(movieListType = MovieListType.PopularMovies) } returns Result.Success(movieListDtos)
        coEvery { movieDao.getMovieById(any<Int>()) } returns movieListEntities.first()
        coEvery { movieDao.upsertMovies(any<List<MovieEntity>>()) } throws Exception()

        repository.getPopularMovies()

        verify { Log.e(any<String>(), any<String>()) }
    }

    @Test
    fun getWatchlist_returns_data_from_database(): Unit = runBlocking {
        coEvery { movieDao.getMovieById(any<Int>()) } returns movieListEntities.first()
        coEvery { dataSource.getWatchlist(any<String>()) } returns Result.Success(movieListDtos)
        coEvery { movieDao.upsertMovies(any<List<MovieEntity>>()) } returns Unit
        coEvery { movieDao.getWatchlist(any<Int>()) } returns movieListEntities

        val result = repository.getWatchlist("session_id")

        Assert.assertTrue(result is Result.Success)
        Assert.assertEquals(movieListEntities.map { it.toMovie() }, (result as Result.Success).data)
    }

    @Test
    fun getWatchlist_returns_database_error(): Unit = runBlocking {
        coEvery { movieDao.getMovieById(any<Int>()) } returns movieListEntities.first()
        coEvery { dataSource.getWatchlist(any<String>()) } returns Result.Success(movieListDtos)
        coEvery { movieDao.upsertMovies(any<List<MovieEntity>>()) } returns Unit
        coEvery { movieDao.getWatchlist(any<Int>()) } throws Exception()

        val result = repository.getWatchlist("session_id")

        Assert.assertTrue(result is Result.Error)
        Assert.assertTrue((result as Result.Error).error == DataError.Database(DatabaseError.FETCH_ERROR))
    }

    @Test
    fun getWatclist_triggers_remote_fetch_then_returns_data_from_database(): Unit = runBlocking {
        val sessionId = "session_id"
        coEvery { movieDao.getMovieById(any<Int>()) } returns movieListEntities.first()
        coEvery { dataSource.getWatchlist(any<String>()) } returns Result.Success(movieListDtos)
        coEvery { movieDao.upsertMovies(any<List<MovieEntity>>()) } returns Unit
        coEvery { movieDao.getWatchlist(any<Int>()) } returnsMany listOf(emptyList(), movieListEntities)

        val result = repository.getWatchlist(sessionId)
        coVerify(exactly = 1) { dataSource.getWatchlist(sessionId, any<Int>()) }
        coVerify(exactly = 2) { movieDao.getWatchlist(any<Int>()) }

        Assert.assertTrue(result is Result.Success)
        Assert.assertEquals(movieListEntities.map { it.toMovie() }, (result as Result.Success).data)
    }

    @Test
    fun getWatclist_triggers_remote_fetch_but_returns_network_error(): Unit = runBlocking {
        val networkError = NetworkError.SERVER_ERROR
        val sessionId = "session_id"
        coEvery { movieDao.getMovieById(any<Int>()) } returns movieListEntities.first()
        coEvery { dataSource.getWatchlist(any<String>()) } returns Result.Error(networkError)
        coEvery { movieDao.upsertMovies(any<List<MovieEntity>>()) } returns Unit
        coEvery { movieDao.getWatchlist(any<Int>()) } returnsMany listOf(emptyList(), movieListEntities)

        val result = repository.getWatchlist(sessionId)
        coVerify(exactly = 1) { dataSource.getWatchlist(sessionId, any<Int>()) }

        Assert.assertTrue(result is Result.Error)
        Assert.assertTrue((result as Result.Error).error == DataError.Network(networkError))
    }

    @Test
    fun search_returns_successfully() = runBlocking {
        coEvery { dataSource.search(any<String>(), any<Int>()) } returns Result.Success(movieListDtos)

        val result = repository.search("searchText")

        Assert.assertTrue(result is Result.Success)
        Assert.assertEquals(movieListDtos.map { it.toMovie() }, (result as Result.Success).data)
    }

    @Test
    fun search_returns_with_error_in_case_of_network_error() = runBlocking {
        val networkError = NetworkError.SERVER_ERROR
        coEvery { dataSource.search(any<String>(), any<Int>()) } returns Result.Error(networkError)

        val result = repository.search("searchText")

        Assert.assertTrue(result is Result.Error)
        Assert.assertTrue((result as Result.Error).error == DataError.Network(networkError))
    }
}