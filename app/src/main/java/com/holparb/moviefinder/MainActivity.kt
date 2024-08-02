package com.holparb.moviefinder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.holparb.moviefinder.movies.presentation.events.HomeEvent
import com.holparb.moviefinder.movies.presentation.pages.HomeScreen
import com.holparb.moviefinder.movies.presentation.viewmodels.PopularMoviesViewModel
import com.holparb.moviefinder.ui.theme.MovieFinderTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: PopularMoviesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        viewModel.onEvent(HomeEvent.LoadPopularMovies)
        setContent {
            MovieFinderTheme {

                val popularMoviesState by viewModel.popularMoviesState.collectAsStateWithLifecycle()
                val mainItemState by viewModel.mainItemState.collectAsStateWithLifecycle()

                HomeScreen(
                    popularMoviesState = popularMoviesState,
                    mainItemState = mainItemState
                )
            }
        }
    }
}