package com.holparb.moviefinder.movies.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.holparb.moviefinder.movies.presentation.components.MainMovieItem
import com.holparb.moviefinder.movies.presentation.components.horizontal_list.MovieHorizontalList
import com.holparb.moviefinder.movies.presentation.events.PopularMoviesEvent
import com.holparb.moviefinder.movies.presentation.viewmodels.PopularMoviesViewModel

@Composable
fun HomeScreen(
    popularMoviesViewModel: PopularMoviesViewModel = hiltViewModel<PopularMoviesViewModel>(),
) {
    val scrollState = rememberScrollState()
    val mainItemState by popularMoviesViewModel.mainItemState.collectAsStateWithLifecycle()
    val popularMoviesState by popularMoviesViewModel.popularMoviesState.collectAsStateWithLifecycle()
    popularMoviesViewModel.onEvent(PopularMoviesEvent.LoadPopularMovies)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState).padding(bottom = 16.dp)
    ) {
        MainMovieItem(
            state = mainItemState,
            modifier = Modifier.height(280.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        MovieHorizontalList(
            state = popularMoviesState,
            title = "Popular movies",
        )
        Spacer(modifier = Modifier.height(16.dp))
        MovieHorizontalList(
            state = popularMoviesState,
            title = "Top rated movies",
        )
        Spacer(modifier = Modifier.height(16.dp))
        MovieHorizontalList(
            state = popularMoviesState,
            title = "Upcoming movies"
        )
    }
}