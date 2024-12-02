package com.holparb.moviefinder.movies.presentation.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.holparb.moviefinder.movies.presentation.details.components.MovieDetailsHeader
import com.holparb.moviefinder.movies.presentation.details.components.previewMovie
import com.holparb.moviefinder.ui.theme.MovieFinderTheme

@Composable
fun MovieDetailsScreen(
    movieDetailsUi: MovieDetailsUi,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
        ) {
            MovieDetailsHeader(movieDetailsUi = movieDetailsUi, modifier = Modifier.height(300.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Column {
                    Text(
                        text = "Story",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = movieDetailsUi.overview
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun MovieDetailsScreenPreview() {
    MovieFinderTheme {
        MovieDetailsScreen(movieDetailsUi = previewMovie.toMovieDetailsUi())
    }
}