package com.holparb.moviefinder.movies.presentation.details.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.holparb.moviefinder.R
import com.holparb.moviefinder.movies.domain.model.Genre
import com.holparb.moviefinder.movies.domain.model.Movie
import com.holparb.moviefinder.movies.presentation.details.MovieDetailsUi
import com.holparb.moviefinder.movies.presentation.details.toMovieDetailsUi
import com.holparb.moviefinder.ui.theme.MovieFinderTheme
import java.time.LocalDate

@Composable
fun MovieDetailsHeader(
    movieDetailsUi: MovieDetailsUi,
    toggleWatchlist: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        SubcomposeAsyncImage(
            model = movieDetailsUi.backdropPath,
            error = {
                val resId = if(LocalInspectionMode.current) {
                    R.drawable.test_backdrop_path
                } else {
                    R.drawable.backdrop_path_error
                }
                Image(
                    painter = painterResource(id = resId),
                    contentDescription = null
                )
            },
            contentDescription = "Movie backdrop path",
            contentScale = ContentScale.FillHeight,
            modifier = Modifier
                .fillMaxSize(),

        )
        Box(
            modifier = Modifier.align(Alignment.TopStart).padding(start = 16.dp, top = 16.dp)
        ) {
            IconButton(
                onClick = { onBack() },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.Gray.copy(alpha = 0.5f))
                    .size(50.dp)
            ) {
                Icon(
                    modifier = Modifier.size(46.dp),
                    imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
        Box(
            modifier = Modifier.align(Alignment.TopEnd).padding(end = 16.dp, top = 16.dp)
        ) {
            IconButton(
                onClick = { toggleWatchlist() },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.Gray.copy(alpha = 0.5f))
                    .size(50.dp)
            ) {
                Icon(
                    modifier = Modifier.size(32.dp),
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    tint = if (movieDetailsUi.isWatchlist) MaterialTheme.colorScheme.primary else Color.White
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.Transparent, Color.Black),
                    startY = 500.0f
                )
            )
        )
        Column (
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .align(Alignment.BottomStart)
        ) {
            Text(
                text = movieDetailsUi.title ,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Text(
                    text = movieDetailsUi.releaseYear
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = movieDetailsUi.genre
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = movieDetailsUi.runtime
                )
            }
            Row {
                Icon(
                    imageVector = Icons.Default.Star,
                    tint = Color.Yellow,
                    contentDescription = "Star icon"
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = movieDetailsUi.rating?.formattedValue ?: stringResource(R.string.no_rating),
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MovieDetailsHeaderPreview() {
    MovieFinderTheme {
        MovieDetailsHeader(movieDetailsUi = previewMovie.toMovieDetailsUi(), onBack = {}, toggleWatchlist = {}, modifier = Modifier.height(400.dp))
    }
}

internal val previewMovie = Movie(
    id = 1,
    title = "Movie Title",
    overview = "This is an overview of a movie which is very good",
    releaseDate = LocalDate.now(),
    genres = listOf(Genre(id = 1, name = "Adventure")),
    rating = 4.6,
    runtime = 136
)