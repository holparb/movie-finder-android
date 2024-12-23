package com.holparb.moviefinder.movies.presentation.components.horizontal_list

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import com.holparb.moviefinder.core.presentation.util.TestTags
import com.holparb.moviefinder.movies.domain.model.MovieListType
import com.holparb.moviefinder.movies.presentation.home_screen.MovieListState
import com.holparb.moviefinder.movies.presentation.home_screen.components.MovieHorizontalList
import com.holparb.moviefinder.ui.theme.MovieFinderTheme
import org.junit.Rule
import org.junit.Test


class MovieHorizontalListTest {

    @get:Rule
    val composeRule = createComposeRule()

    private fun setContent(state: MovieListState = MovieListState(movieListType = MovieListType.PopularMovies), title: String = "Title") {
        composeRule.setContent {
            val navController = rememberNavController()
            MovieFinderTheme {
                MovieHorizontalList(
                    state = state,
                    title = title,
                    navController = navController
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
        setContent()
        composeRule.onNodeWithText("Items failed to load, please check your network connection and try again").assertIsNotDisplayed()
        composeRule.onNodeWithTag(TestTags.MOVIE_HORIZONTAL_LIST_ITEMS).assertIsNotDisplayed()
        composeRule.onNodeWithTag(TestTags.MOVIE_HORIZONTAL_LIST_LOADING).assertIsDisplayed()
    }

    @Test
    fun error_state_displays_error_text() {
        //setContent(MovieListState(movieList = DataLoadState.Error(error = MovieError.NetworkError(""))))
        composeRule.onNodeWithTag(TestTags.MOVIE_HORIZONTAL_LIST_ITEMS).assertIsNotDisplayed()
        composeRule.onNodeWithTag(TestTags.MOVIE_HORIZONTAL_LIST_LOADING).assertIsNotDisplayed()
        composeRule.onNodeWithText("Items failed to load, please check your network connection and try again").assertIsDisplayed()
    }
}