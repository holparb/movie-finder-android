package com.holparb.moviefinder.movies.presentation.components.horizontal_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.holparb.moviefinder.movies.domain.model.MovieListItem

@Composable
fun MovieHorizontalList(
    movies: List<MovieListItem>,
    title: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp)
    ) {
        SectionHeader(
            title = title, onClick = {}
        )
        Spacer(modifier = Modifier.height(12.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(movies) { item ->
                MovieHorizontalListItem(
                    movie = item,
                    modifier = Modifier
                        .width(120.dp)
                        .height(180.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun MovieHorizontalListPreview() {
    MovieHorizontalList(movies = emptyList(), modifier = Modifier.height(250.dp), title = "Title")
}