package com.holparb.moviefinder.movies.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.holparb.moviefinder.movies.domain.model.MovieListItem

@Composable
fun MovieHorizontalList(
    movies: List<MovieListItem>,
    paddingValues: PaddingValues,
    title: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(12.dp))
        LazyRow(
            contentPadding = paddingValues,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(movies) { item ->
                MovieHorizontalListItem(
                    movie = item,
                    modifier = Modifier
                        .width(150.dp)
                        .height(225.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun MovieHorizontalListPreview() {
    MovieHorizontalList(movies = emptyList(), modifier = Modifier.height(250.dp), title = "Title", paddingValues = PaddingValues())
}