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
    fun writeMoviesAndGetList() = runTest {
        val movies = listOf(
            MovieEntity(
                id = 1,
                title = "title1",
                overview = "overview",
                releaseDate = "2023-03-12",
                posterPath = "posterPath",
                backdropPath = "backdropPath",
                genreIds = listOf(1,2,3),
                isPopular = true
            ),
            MovieEntity(
                id = 2,
                title = "title2",
                overview = "overview",
                releaseDate = "2024-09-25",
                posterPath = "posterPath",
                backdropPath = "backdropPath",
                genreIds = listOf(1,2,3),
                isPopular = true
            ),
            MovieEntity(
                id = 3,
                title = "title3",
                overview = "overview",
                releaseDate = "2000-01-01",
                posterPath = "posterPath",
                backdropPath = "backdropPath",
                genreIds = listOf(1,2,3),
                isPopular = true
            )
        )

        movieDao.upsertMovies(movies)

        movieDao.getPopularMoviesForHomeScreen().test {
            val result = awaitItem()
            assert(result.size == 3)
            cancel()
        }
    }
}