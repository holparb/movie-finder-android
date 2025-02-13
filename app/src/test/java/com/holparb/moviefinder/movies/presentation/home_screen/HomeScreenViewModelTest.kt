package com.holparb.moviefinder.movies.presentation.home_screen

import app.cash.turbine.test
import com.holparb.moviefinder.core.domain.util.Result
import com.holparb.moviefinder.core.domain.util.errors.DataError
import com.holparb.moviefinder.core.domain.util.errors.NetworkError
import com.holparb.moviefinder.movies.domain.model.Movie
import com.holparb.moviefinder.movies.domain.model.MovieListType
import com.holparb.moviefinder.movies.domain.repository.MovieRepository
import com.holparb.moviefinder.movies.domain.util.MovieEvent
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeScreenViewModelTest {

    private lateinit var repository: MovieRepository
    private val viewModel: HomeScreenViewModel by lazy {
        HomeScreenViewModel(repository)
    }
    private val movies: List<Movie> = listOf(
        Movie(id = 1, title = "title", overview = "overview"),
        Movie(id = 2, title = "title", overview = "overview"),
        Movie(id = 3, title = "title", overview = "overview"),
        Movie(id = 4, title = "title", overview = "overview"),
        Movie(id = 5, title = "title", overview = "overview")
    )

    @Before
    fun setup() {
        repository = mockk()
        //viewModel = HomeScreenViewModel(repository)
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun tearDown() {
        clearAllMocks()
        Dispatchers.resetMain()
    }

    @Test
    fun repository_functions_should_be_called_on_init() {
        coEvery { repository.getPopularMovies(any<Int>(), any<String>()) } returns Result.Success(data = movies)
        coEvery { repository.getUpcomingMovies(any<Int>(), any<String>()) } returns Result.Success(data = movies)
        coEvery { repository.getTopRatedMovies(any<Int>(), any<String>()) } returns Result.Success(data = movies)
        viewModel.state.value.movieLists.forEach { (key, value) ->
            coVerify(exactly = 1) { repository.getPopularMovies(any<Int>(), any<String>()) }
            coVerify(exactly = 1) { repository.getTopRatedMovies(any<Int>(), any<String>()) }
            coVerify(exactly = 1) { repository.getUpcomingMovies(any<Int>(), any<String>()) }
            when (key) {
                HomeScreenState.MAIN_ITEM, HomeScreenState.POPULAR_MOVIES -> {
                    Assert.assertTrue(value.movieListType == MovieListType.PopularMovies)
                }
                HomeScreenState.TOP_RATED_MOVIES -> {
                    Assert.assertTrue(value.movieListType == MovieListType.TopRatedMovies)
                }
                HomeScreenState.UPCOMING_MOVIES -> {
                    Assert.assertTrue(value.movieListType == MovieListType.UpcomingMovies)
                }
            }
        }
    }

    @Test
    fun movies_are_loaded_from_network_successfully(): Unit = runTest {
        coEvery { repository.getPopularMovies(any<Int>(), any<String>()) } coAnswers {
            delay(500)
            Result.Success(data = movies)
        }
        coEvery { repository.getUpcomingMovies(any<Int>(), any<String>()) } coAnswers {
            delay(500)
            Result.Success(data = movies)
        }
        coEvery { repository.getTopRatedMovies(any<Int>(), any<String>()) } coAnswers {
            delay(500)
            Result.Success(data = movies)
        }

        val mainItem = viewModel.state.value.movieLists[HomeScreenState.MAIN_ITEM]!!
        val popularMovies = viewModel.state.value.movieLists[HomeScreenState.POPULAR_MOVIES]!!
        val topRatedMovies = viewModel.state.value.movieLists[HomeScreenState.UPCOMING_MOVIES]!!
        val upcomingMovies = viewModel.state.value.movieLists[HomeScreenState.TOP_RATED_MOVIES]!!

        Assert.assertNotNull(mainItem)
        Assert.assertTrue(mainItem.isLoading)
        Assert.assertTrue(popularMovies.isLoading)
        Assert.assertTrue(topRatedMovies.isLoading)
        Assert.assertTrue(upcomingMovies.isLoading)
        advanceUntilIdle()
        viewModel.state.test {
            val updatedState = awaitItem()
            val mainItemAfterDelay = updatedState.movieLists[HomeScreenState.MAIN_ITEM]!!
            val popularMoviesAfterDelay = updatedState.movieLists[HomeScreenState.POPULAR_MOVIES]!!
            val topRatedMoviesAfterDelay = updatedState.movieLists[HomeScreenState.UPCOMING_MOVIES]!!
            val upcomingMoviesAfterDelay = updatedState.movieLists[HomeScreenState.TOP_RATED_MOVIES]!!

            Assert.assertFalse(mainItemAfterDelay.isLoading)
            Assert.assertFalse(popularMoviesAfterDelay.isLoading)
            Assert.assertFalse(topRatedMoviesAfterDelay.isLoading)
            Assert.assertFalse(upcomingMoviesAfterDelay.isLoading)
            Assert.assertEquals(movies, mainItemAfterDelay.movieList)
            Assert.assertEquals(movies, popularMoviesAfterDelay.movieList)
            Assert.assertEquals(movies, topRatedMoviesAfterDelay.movieList)
            Assert.assertEquals(movies, upcomingMoviesAfterDelay.movieList)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun no_internet_connection(): Unit = runTest {
        val error = DataError.Network(NetworkError.NO_INTERNET_CONNECTION)
        coEvery { repository.getPopularMovies(any<Int>(), any<String>()) } coAnswers {
            delay(200)
            Result.Error(error = error)
        }
        coEvery { repository.getUpcomingMovies(any<Int>(), any<String>()) } coAnswers {
            delay(200)
            Result.Error(error = error)
        }
        coEvery { repository.getTopRatedMovies(any<Int>(), any<String>()) } coAnswers {
            delay(200)
            Result.Error(error = error)
        }

        val mainItem = viewModel.state.value.movieLists[HomeScreenState.MAIN_ITEM]!!
        val popularMovies = viewModel.state.value.movieLists[HomeScreenState.POPULAR_MOVIES]!!
        val topRatedMovies = viewModel.state.value.movieLists[HomeScreenState.UPCOMING_MOVIES]!!
        val upcomingMovies = viewModel.state.value.movieLists[HomeScreenState.TOP_RATED_MOVIES]!!

        Assert.assertTrue(mainItem.isLoading)
        Assert.assertTrue(popularMovies.isLoading)
        Assert.assertTrue(topRatedMovies.isLoading)
        Assert.assertTrue(upcomingMovies.isLoading)

        advanceUntilIdle()
        viewModel.state.test {
            val updatedState = awaitItem()
            val mainItemAfterDelay = updatedState.movieLists[HomeScreenState.MAIN_ITEM]!!
            val popularMoviesAfterDelay = updatedState.movieLists[HomeScreenState.POPULAR_MOVIES]!!
            val topRatedMoviesAfterDelay = updatedState.movieLists[HomeScreenState.UPCOMING_MOVIES]!!
            val upcomingMoviesAfterDelay = updatedState.movieLists[HomeScreenState.TOP_RATED_MOVIES]!!

            Assert.assertFalse(mainItemAfterDelay.isLoading)
            Assert.assertFalse(popularMoviesAfterDelay.isLoading)
            Assert.assertFalse(topRatedMoviesAfterDelay.isLoading)
            Assert.assertFalse(upcomingMoviesAfterDelay.isLoading)

            cancelAndIgnoreRemainingEvents()
        }

        viewModel.events.test {
            Assert.assertEquals(MovieEvent.RemoteError(NetworkError.NO_INTERNET_CONNECTION), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
