package com.holparb.moviefinder.movies.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.holparb.moviefinder.R
import com.holparb.moviefinder.movies.domain.model.MovieListItem
import com.holparb.moviefinder.ui.theme.Shapes
import java.time.LocalDateTime

@Composable
fun MovieHorizontalListItem(
    movie: MovieListItem,
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(id = R.drawable.test_poster),
        contentDescription = "movie poster",
        modifier = modifier.fillMaxSize().clip(Shapes.medium).aspectRatio(2f / 3f)
    )
}

@Preview
@Composable
private fun MovieHorizontalListItemPreview() {
    MovieHorizontalListItem(
        MovieListItem(id = 1, title = "Title", releaseDate = LocalDateTime.now(), posterPath = ""),
        modifier = Modifier.width(150.dp).height(225.dp))
}