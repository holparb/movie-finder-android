package com.holparb.moviefinder.movies.presentation.home_screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.holparb.moviefinder.R
import com.holparb.moviefinder.ui.theme.Shapes

@Composable
fun MoviePosterPicture(
    posterPath: String?,
    modifier: Modifier = Modifier
) {
    SubcomposeAsyncImage(
        model = posterPath,
        contentDescription = "movie poster",
        error = {
            Image(
                painter = painterResource(id = R.drawable.poster_not_found),
                contentDescription = "poster placeholder"
            )
        },
        modifier = modifier
            .clip(Shapes.medium)
            .aspectRatio(2f / 3f)
    )
}

@Preview
@Composable
private fun MovieHorizontalListItemPreview() {
    MoviePosterPicture(
        "",
        modifier = Modifier
            .width(150.dp)
            .height(225.dp))
}