package com.holparb.moviefinder.movies.presentation.viewmodels

import app.cash.turbine.test
import com.holparb.moviefinder.movies.domain.model.Movie
import com.holparb.moviefinder.movies.domain.repository.MovieRepository
import com.holparb.moviefinder.movies.domain.util.MovieError
import com.holparb.moviefinder.core.utils.Resource
import com.holparb.moviefinder.movies.presentation.home_screen.HomeScreenViewModel
import com.holparb.moviefinder.movies.presentation.states.DataLoadState
import com.holparb.moviefinder.movies.presentation.home_screen.HomeScreenState
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.DelicateCoroutinesApi
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
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
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
    @OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)

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
    fun view_model_screen_states_should_be_loading_by_default() {
        viewModel.state.value.movieLists.forEach { (_, value) ->
            Assert.assertTrue(value.movieList is DataLoadState.Loading)
        }
    }

    @Test
    fun each_movie_list_is_assigned_correct_load_event() {
        viewModel.state.value.movieLists.forEach { (key, value) ->
            if(key == HomeScreenState.MAIN_ITEM || key == HomeScreenState.POPULAR_MOVIES) {
                Assert.assertTrue(value.loadEvent is MovieListLoadEvent.LoadPopularMovies)
            }
            if(key == HomeScreenState.TOP_RATED_MOVIES) {
                Assert.assertTrue(value.loadEvent is MovieListLoadEvent.LoadTopRatedMovies)
            }
            if(key == HomeScreenState.UPCOMING_MOVIES) {
                Assert.assertTrue(value.loadEvent is MovieListLoadEvent.LoadUpcomingMovies)
            }
        }
    }

    @Test
    fun loadPopularMoviesAndMainItem_loads_main_item_successfully(): Unit = runTest {
        coEvery { repository.getPopularMovies(any<Int>(), any<String>()) } coAnswers {
            delay(500)
            Resource.Success(data = movies)
        }

        viewModel.loadPopularMoviesAndMainItem()

        val mainItem = viewModel.state.value.movieLists[HomeScreenState.MAIN_ITEM]?.movieList
        Assert.assertTrue(mainItem is DataLoadState.Loading)
        advanceUntilIdle()
        viewModel.state.test {
            val updatedState = awaitItem()
            val mainItemAfterDelay = updatedState.movieLists[HomeScreenState.MAIN_ITEM]?.movieList

            Assert.assertTrue(mainItemAfterDelay is DataLoadState.Loaded)
            Assert.assertEquals(movies, (mainItemAfterDelay as DataLoadState.Loaded).data)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun loadPopularMoviesAndMainItem_loads_popular_movies_successfully(): Unit = runTest {
        coEvery { repository.getPopularMovies(any<Int>(), any<String>()) } coAnswers {
            delay(500)
            Resource.Success(data = movies)
        }

        viewModel.loadPopularMoviesAndMainItem()

        val popularMovies = viewModel.state.value.movieLists[HomeScreenState.POPULAR_MOVIES]?.movieList
        Assert.assertTrue(popularMovies is DataLoadState.Loading)
        advanceUntilIdle()
        viewModel.state.test {
            val updatedState = awaitItem()
            val popularMoviesAfterDelay = updatedState.movieLists[HomeScreenState.MAIN_ITEM]?.movieList

            Assert.assertTrue(popularMoviesAfterDelay is DataLoadState.Loaded)
            Assert.assertEquals(movies, (popularMoviesAfterDelay as DataLoadState.Loaded).data)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun loadPopularMoviesAndMainItem_returns_error_for_main_item_and_popular_movies(): Unit = runBlocking {
        val error = MovieError.NetworkError("Error during network call")
        coEvery { repository.getPopularMovies(any<Int>(), any<String>()) } returns Resource.Error(error = error)

        viewModel.loadPopularMoviesAndMainItem()

        val mainItem = viewModel.state.value.movieLists[HomeScreenState.MAIN_ITEM]?.movieList
        Assert.assertTrue(mainItem is DataLoadState.Error)
        Assert.assertEquals(error, (mainItem as DataLoadState.Error).error)

        val popularMovies = viewModel.state.value.movieLists[HomeScreenState.POPULAR_MOVIES]?.movieList
        Assert.assertTrue(popularMovies is DataLoadState.Error)
        Assert.assertEquals(error, (popularMovies as DataLoadState.Error).error)
    }
}
