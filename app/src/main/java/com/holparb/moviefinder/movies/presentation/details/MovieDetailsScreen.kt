package com.holparb.moviefinder.movies.presentation.details

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.holparb.moviefinder.core.presentation.util.ObserveAsEvents
import com.holparb.moviefinder.core.presentation.util.toString
import com.holparb.moviefinder.movies.domain.util.MovieEvent
import com.holparb.moviefinder.movies.presentation.details.components.MovieDetailsDisplay

@Composable
fun MovieDetailsScreen(
    modifier: Modifier = Modifier
) {
    val movieDetailsViewModel = hiltViewModel<MovieDetailsViewModel>()
    val state by movieDetailsViewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    ObserveAsEvents(events = movieDetailsViewModel.events) { event ->
        val toastText = when(event) {
            is MovieEvent.LocalError -> event.error.toString(context)
            is MovieEvent.RemoteError -> event.error.toString(context)
        }
        Toast.makeText(
            context,
            toastText,
            Toast.LENGTH_LONG
        ).show()
    }
    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        MovieDetailsDisplay(
            movieDetailsUi = state.movieDetailsUi,
            modifier = Modifier.padding(paddingValues)
        )
    }
}