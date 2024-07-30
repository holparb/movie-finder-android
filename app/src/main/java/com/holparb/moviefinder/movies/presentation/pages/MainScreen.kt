package com.holparb.moviefinder.movies.presentation.pages

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.holparb.moviefinder.movies.presentation.components.MovieHorizontalList
import com.holparb.moviefinder.movies.presentation.states.MainItemState
import com.holparb.moviefinder.movies.presentation.states.MovieListState

@Composable
fun MainScreen(
    mainItemState: MainItemState,
    popularMoviesState: MovieListState
) {
    Surface(
        modifier = Modifier.fillMaxSize().statusBarsPadding(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize().statusBarsPadding().padding(16.dp),
        ) { paddingValues ->
            MovieHorizontalList(movies = popularMoviesState.movies, title = "Title", paddingValues = paddingValues)
        }
    }
}