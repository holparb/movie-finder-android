package com.holparb.moviefinder.movies.presentation.watchlist

import app.cash.turbine.test
import com.holparb.moviefinder.core.domain.util.LocalEncryptedStorage
import com.holparb.moviefinder.core.domain.util.pagination.MovieListPaginator
import com.holparb.moviefinder.di.MovieListPaginatorFactory
import com.holparb.moviefinder.movies.domain.repository.MovieRepository
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WatchlistViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var localEncryptedStorage: LocalEncryptedStorage
    private lateinit var movieRepository: MovieRepository
    private lateinit var movieListPaginatorFactory: MovieListPaginatorFactory
    private lateinit var movieListPaginator: MovieListPaginator
    private lateinit var viewModel: WatchlistViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        localEncryptedStorage = mockk()
        movieRepository = mockk()
        movieListPaginatorFactory = mockk()
        movieListPaginator = mockk(relaxed = true)
        every { movieListPaginatorFactory.create(any(), any(), any(), any()) } returns movieListPaginator
        viewModel = WatchlistViewModel(
            localEncryptedStorage = localEncryptedStorage,
            movieRepository = movieRepository,
            movieListPaginatorFactory = movieListPaginatorFactory
        )
    }

    @After
    fun tearDown() {
        clearAllMocks()
        Dispatchers.resetMain()
    }

    @Test
    fun initial_states_are_correct() {
        assert(!viewModel.state.value.isUserLoggedIn)
        assert(viewModel.state.value.movies.isEmpty())
        assert(!viewModel.state.value.isNewPageLoading)
        assert(!viewModel.state.value.isLastPageReached)
    }

    @Test
    fun watchlist_loading_started_on_flow_collection_when_user_is_logged_in() = runTest {
        every { localEncryptedStorage.getSessionId() } returns "session_id"
        coEvery { movieListPaginator.loadNextPage() } returns Unit

        Assert.assertTrue(viewModel.state.value.isUserLoggedIn.not())
        val job = launch { viewModel.state.collect() }
        viewModel.state.test {
            coVerify { movieListPaginator.loadNextPage() }

            val updatedState = awaitItem()
            Assert.assertTrue(updatedState.isUserLoggedIn)

            cancelAndIgnoreRemainingEvents()
        }
        job.cancel()
    }

    @Test
    fun watchlist_loading_is_not_started_on_flow_collection_when_user_is_not_logged_in() = runTest {
        every { localEncryptedStorage.getSessionId() } returns null
        coEvery { movieListPaginator.loadNextPage() } returns Unit

        Assert.assertTrue(viewModel.state.value.isUserLoggedIn.not())
        val job = launch { viewModel.state.collect() }
        viewModel.state.test {
            coVerify(exactly = 0) { movieListPaginator.loadNextPage() }

            val updatedState = awaitItem()
            Assert.assertTrue(updatedState.isUserLoggedIn.not())

            cancelAndIgnoreRemainingEvents()
        }
        job.cancel()
    }
}