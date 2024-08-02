package com.holparb.moviefinder.movies.presentation.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.holparb.moviefinder.movies.presentation.components.MainMovieItem
import com.holparb.moviefinder.movies.presentation.components.horizontal_list.MovieHorizontalList
import com.holparb.moviefinder.movies.presentation.states.MainItemState
import com.holparb.moviefinder.movies.presentation.states.MovieListState

@Composable
fun HomeScreen(
    mainItemState: MainItemState,
    popularMoviesState: MovieListState
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                //.statusBarsPadding()
        ) { paddingValues ->
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
            ) {
                MainMovieItem(
                    state = mainItemState,
                    modifier = Modifier.height(280.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                MovieHorizontalList(
                    movies = popularMoviesState.movies,
                    title = "Popular movies",
                    paddingValues = paddingValues
                )
                Spacer(modifier = Modifier.height(16.dp))
                MovieHorizontalList(
                    movies = popularMoviesState.movies,
                    title = "Top rated movies",
                    paddingValues = paddingValues
                )
                Spacer(modifier = Modifier.height(16.dp))
                MovieHorizontalList(
                    movies = popularMoviesState.movies,
                    title = "Upcoming movies",
                    paddingValues = paddingValues
                )
            }
        }
    }
}