package com.holparb.moviefinder.movies.presentation.details.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.holparb.moviefinder.movies.presentation.details.MovieDetailsUi
import com.holparb.moviefinder.movies.presentation.details.toMovieDetailsUi
import com.holparb.moviefinder.ui.theme.MovieFinderTheme

@Composable
fun MovieDetailsDisplay(
    movieDetailsUi: MovieDetailsUi,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        MovieDetailsHeader(
            movieDetailsUi = movieDetailsUi,
            modifier = Modifier.height(300.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        MovieDetailsBody(
            movieDetailsUi = movieDetailsUi,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview
@Composable
private fun MovieDetailsDisplayPreview() {
    MovieFinderTheme {
        MovieDetailsDisplay(movieDetailsUi = previewMovie.toMovieDetailsUi())
    }
}