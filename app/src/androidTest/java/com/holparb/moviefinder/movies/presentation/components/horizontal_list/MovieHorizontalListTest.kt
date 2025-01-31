package com.holparb.moviefinder.movies.presentation.components.horizontal_list

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasStateDescription
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import com.holparb.moviefinder.R
import com.holparb.moviefinder.movies.domain.model.Movie
import com.holparb.moviefinder.movies.domain.model.MovieListType
import com.holparb.moviefinder.movies.presentation.home_screen.MovieListState
import com.holparb.moviefinder.movies.presentation.home_screen.components.MovieHorizontalList
import com.holparb.moviefinder.ui.theme.MovieFinderTheme
import io.mockk.clearAllMocks
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.random.Random


class MovieHorizontalListTest {

    @get:Rule
    val composeRule = createComposeRule()

    private lateinit var onNavigateToMovieDetails: (Int) -> Unit
    private lateinit var onNavigateToSeeMoreScreen: (String, MovieListType) -> Unit

    private lateinit var context: Context

    @Before
    fun setUp() {
        onNavigateToMovieDetails = mockk<(Int) -> Unit>(relaxed = true)
        onNavigateToSeeMoreScreen = mockk<(String, MovieListType) -> Unit>(relaxed = true)
        context = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    private fun setContent(state: MovieListState = MovieListState(movieListType = MovieListType.PopularMovies), title: String = "Title") {
        composeRule.setContent {
            MovieFinderTheme {
                MovieHorizontalList(
                    state = state,
                    title = title,
                    onNavigateToMovieDetails = onNavigateToMovieDetails,
                    onNavigateToSeeMoreScreen = onNavigateToSeeMoreScreen
                )
            }
        }
    }

    @Test
    fun section_header_rendered() {
        val title = "TestTitle"
        setContent(title = title)
        composeRule.onNodeWithText(title).assertIsDisplayed()
        composeRule.onNodeWithText("see more >").assertIsDisplayed()
    }

    @Test
    fun loading_state_displays_progress_indicator() {
        setContent(state = MovieListState(isLoading = true, movieListType = MovieListType.PopularMovies))
        composeRule.onNode(hasStateDescription(context.resources.getString(R.string.loaded_description))).assertIsNotDisplayed()
        composeRule.onNode(hasStateDescription(context.resources.getString(R.string.loading_description))).assertIsDisplayed()
    }

    @Test
    fun see_more_is_clicked() {
        val movieListType = MovieListType.UpcomingMovies
        val title = "Upcoming movies"
        setContent(title = title, state = MovieListState(movieListType = movieListType))
        composeRule.onNodeWithText("see more >").performClick()

        verify(exactly = 1) { onNavigateToSeeMoreScreen(title, movieListType) }
    }

    @Test
    fun movie_item_is_clicked() {
        val movies = listOf<Movie>(
            Movie(id = 1, title = "Movie", overview = "overview"),
            Movie(id = 2, title = "Movie2", overview = "overview"),
            Movie(id = 3, title = "Movie3", overview = "overview")
        )
        setContent(
            state = MovieListState(
                isLoading = false,
                movieListType = MovieListType.PopularMovies,
                movieList = movies
            )
        )
        val index = Random.nextInt(from = 0, until = movies.size)
        composeRule.onAllNodes(hasContentDescription("movie poster"))[index].performClick()
        verify(exactly = 1) { onNavigateToMovieDetails(any()) }
    }
}