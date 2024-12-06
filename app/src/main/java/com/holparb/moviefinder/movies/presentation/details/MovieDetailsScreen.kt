package com.holparb.moviefinder.movies.presentation.details

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.holparb.moviefinder.movies.presentation.details.components.MovieDetailsDisplay

@Composable
fun MovieDetailsScreen(
    modifier: Modifier = Modifier
) {
    val movieDetailsViewModel = hiltViewModel<MovieDetailsViewModel>()
    val state by movieDetailsViewModel.state.collectAsStateWithLifecycle()
    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        MovieDetailsDisplay(
            movieDetailsUi = state.movieDetailsUi,
            modifier = Modifier.padding(paddingValues)
        )
    }
}