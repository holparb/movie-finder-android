package com.holparb.moviefinder.movies.presentation.home_screen.components

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.holparb.moviefinder.movies.domain.model.Movie
import com.holparb.moviefinder.ui.theme.Shapes
import java.time.LocalDate

@Composable
fun MoviePosterPicture(
    movie: Movie,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = movie.posterPath,
        error = rememberVectorPainter(image = Icons.Sharp.Warning),
        contentDescription = "movie poster",
        modifier = modifier
            .clip(Shapes.medium)
            .aspectRatio(2f / 3f)
    )
}

@Preview
@Composable
private fun MovieHorizontalListItemPreview() {
    MoviePosterPicture(
        Movie(id = 1, title = "Title", releaseDate = LocalDate.now(), posterPath = "", overview = "", backdropPath = ""),
        modifier = Modifier
            .width(150.dp)
            .height(225.dp))
}