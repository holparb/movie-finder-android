package com.holparb.moviefinder.movies.presentation.home_screen

import app.cash.turbine.test
import com.holparb.moviefinder.core.domain.util.Result
import com.holparb.moviefinder.movies.domain.model.Movie
import com.holparb.moviefinder.movies.domain.model.MovieListType
import com.holparb.moviefinder.movies.domain.repository.MovieRepository
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
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
    private lateinit var viewModel: HomeScreenViewModel
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
        viewModel = HomeScreenViewModel(repository)
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun tearDown() {
        clearAllMocks()
        Dispatchers.resetMain()
    }

    @Test
    fun movie_list_states_default_values_should_be_correct() {
        viewModel.state.value.movieLists.forEach { (key, value) ->
            Assert.assertFalse(value.isLoading)
            Assert.assertEquals(emptyList<Movie>(), value.movieList)
            if(key == HomeScreenState.MAIN_ITEM || key == HomeScreenState.POPULAR_MOVIES) {
                Assert.assertTrue(value.movieListType == MovieListType.PopularMovies)
            }
            else if(key == HomeScreenState.TOP_RATED_MOVIES) {
                Assert.assertTrue(value.movieListType == MovieListType.TopRatedMovies)
            }
            else if(key == HomeScreenState.UPCOMING_MOVIES) {
                Assert.assertTrue(value.movieListType == MovieListType.UpcomingMovies)
            }
        }
    }

    @Test
    fun loadPopularMoviesAndMainItem_loads_main_item_successfully(): Unit = runTest {
        coEvery { repository.getPopularMovies(any<Int>(), any<String>()) } coAnswers {
            delay(500)
            Result.Success(data = movies)
        }
        coEvery { repository.getUpcomingMovies(any(), any()) } returns Result.Success(data = movies)
        coEvery { repository.getTopRatedMovies(any(), any()) } returns Result.Success(data = movies)

        viewModel.loadHomeScreenContent()

        val mainItem = viewModel.state.value.movieLists[HomeScreenState.MAIN_ITEM]!!
        Assert.assertNotNull(mainItem)
        Assert.assertTrue(mainItem.isLoading)
        advanceUntilIdle()
        viewModel.state.test {
            val updatedState = awaitItem()
            val mainItemAfterDelay = updatedState.movieLists[HomeScreenState.MAIN_ITEM]!!

            Assert.assertFalse(mainItemAfterDelay.isLoading)
            Assert.assertEquals(movies, mainItemAfterDelay.movieList)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun loadPopularMoviesAndMainItem_loads_popular_movies_successfully(): Unit = runTest {
        coEvery { repository.getPopularMovies(any<Int>(), any<String>()) } coAnswers {
            delay(500)
            Result.Success(data = movies)
        }

        //viewModel.loadPopularMoviesAndMainItem()

        val popularMovies = viewModel.state.value.movieLists[HomeScreenState.POPULAR_MOVIES]?.movieList
        //Assert.assertTrue(popularMovies is DataLoadState.Loading)
        advanceUntilIdle()
        viewModel.state.test {
            val updatedState = awaitItem()
            val popularMoviesAfterDelay = updatedState.movieLists[HomeScreenState.MAIN_ITEM]?.movieList

            //Assert.assertTrue(popularMoviesAfterDelay is DataLoadState.Loaded)
            //Assert.assertEquals(movies, (popularMoviesAfterDelay as DataLoadState.Loaded).data)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun loadPopularMoviesAndMainItem_returns_error_for_main_item_and_popular_movies(): Unit = runBlocking {
        /*
        val error = MovieError.NetworkError("Error during network call")
        coEvery { repository.getPopularMovies(any<Int>(), any<String>()) } returns Resource.Error(error = error)

        viewModel.loadPopularMoviesAndMainItem()

        val mainItem = viewModel.state.value.movieLists[HomeScreenState.MAIN_ITEM]?.movieList
        Assert.assertTrue(mainItem is DataLoadState.Error)
        Assert.assertEquals(error, (mainItem as DataLoadState.Error).error)

        val popularMovies = viewModel.state.value.movieLists[HomeScreenState.POPULAR_MOVIES]?.movieList
        Assert.assertTrue(popularMovies is DataLoadState.Error)
        Assert.assertEquals(error, (popularMovies as DataLoadState.Error).error)
        */
    }
}
