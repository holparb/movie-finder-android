package com.holparb.moviefinder.movies.data.local.dao

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import app.cash.turbine.test
import com.holparb.moviefinder.movies.data.dao.MovieDao
import com.holparb.moviefinder.movies.data.datasource.local.MovieDatabase
import com.holparb.moviefinder.movies.data.entity.MovieEntity
import com.holparb.moviefinder.testutils.TestDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
@SmallTest
class MovieDaoTest {
    @get: Rule
    val dispatcherRule = TestDispatcherRule()

    private lateinit var movieDao: MovieDao
    private lateinit var movieDatabase: MovieDatabase
    private val movies = listOf(
        MovieEntity(
            id = 1,
            title = "title1",
            overview = "overview",
            releaseDate = "2023-03-12",
            posterPath = "posterPath",
            backdropPath = "backdropPath",
            genreIds = listOf(1,2,3),
        ),
        MovieEntity(
            id = 2,
            title = "title2",
            overview = "overview",
            releaseDate = "2024-09-25",
            posterPath = "posterPath",
            backdropPath = "backdropPath",
            genreIds = listOf(1,2,3),
        ),
        MovieEntity(
            id = 3,
            title = "title3",
            overview = "overview",
            releaseDate = "2000-01-01",
            posterPath = "posterPath",
            backdropPath = "backdropPath",
            genreIds = listOf(1,2,3),
        )
    )

    private fun createMovieListOfSpecifiedSite(size: Int, isPopular: Boolean = false, isTopRated: Boolean = false, isUpcoming: Boolean = false): List<MovieEntity> {
        val movies = mutableListOf<MovieEntity>()

        for (i in 1..size ) {
            val movie = MovieEntity(
                id = i,
                title = "title",
                overview = "overview",
                releaseDate = "2024-09-25",
                posterPath = "posterPath",
                backdropPath = "backdropPath",
                genreIds = listOf(1,2,3),
                isPopular = isPopular,
                isTopRated = isTopRated,
                isUpcoming = isUpcoming
            )
            movies.add(movie)
        }

        println("Generated size: $movies")

        return movies
    }

    @Before
    fun setup() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        movieDatabase = Room.inMemoryDatabaseBuilder(
            appContext,
            MovieDatabase::class.java
        ).build()
        movieDao = movieDatabase.movieDao
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        movieDatabase.close()
    }

    @Test
    @Throws(Exception::class)
    fun get_popular_movies_for_home_screen_successfully() = runTest {
        val popularMovies = movies.map {
            it.copy(isPopular = true)
        }

        movieDao.upsertMovies(popularMovies)

        movieDao.getPopularMoviesForHomeScreen().test {
            val result = awaitItem()
            assert(result.size == 3)
            cancel()
        }
    }

    @Test
    @Throws(Exception::class)
    fun getPopularMoviesForHomeScreen_returns_movies_in_correct_order() = runTest {
        val popularMovies = movies.map {
            it.copy(isPopular = true)
        }

        movieDao.upsertMovies(popularMovies)

        movieDao.getPopularMoviesForHomeScreen().test {
            val result = awaitItem()
            assert(result[0].id == 2)
            assert(result[1].id == 1)
            assert(result[2].id == 3)
            cancel()
        }
    }

    @Test
    fun getPopularMoviesForHomeScreen_returns_no_more_than_20_items() = runTest {
        val popularMovies = createMovieListOfSpecifiedSite(50, isPopular = true)

        movieDao.upsertMovies(popularMovies)

        movieDao.getPopularMoviesForHomeScreen().test {
            val result = awaitItem()
            assert(result.isNotEmpty() && result.size <= 20)
            cancel()
        }
    }

    @Test
    @Throws(Exception::class)
    fun clearAll_clears_all_movies() = runTest {
        val size = 30
        val popularMovies = createMovieListOfSpecifiedSite(size = size, isPopular = true)

        movieDao.upsertMovies(popularMovies)

        movieDao.getPopularMoviesForHomeScreen().test {
            val result = awaitItem()
            assert(result.size == 20)
            cancel()
        }
        movieDao.clearAll()
        movieDao.getPopularMoviesForHomeScreen().test {
            val result = awaitItem()
            assert(result.isEmpty())
            cancel()
        }
    }
}