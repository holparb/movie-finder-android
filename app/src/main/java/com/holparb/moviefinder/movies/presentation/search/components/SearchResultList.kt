package com.holparb.moviefinder.movies.presentation.search.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.holparb.moviefinder.movies.domain.model.Movie
import com.holparb.moviefinder.movies.presentation.details.components.previewMovie
import com.holparb.moviefinder.movies.presentation.home_screen.components.MoviePosterPicture
import com.holparb.moviefinder.ui.theme.MovieFinderTheme

@Composable
fun SearchResultList(
    movies: List<Movie>,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(120.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxSize()
    ) {
        items(movies) { movie ->
            MoviePosterPicture(
                posterPath = movie.posterPath
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchResultListPreview() {
    MovieFinderTheme {
        SearchResultList(
            movies = listOf(
                previewMovie,
                previewMovie,
                previewMovie,
                previewMovie,
                previewMovie
            )
        )
    }
}