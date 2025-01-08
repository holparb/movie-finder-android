package com.holparb.moviefinder.movies.presentation.see_more.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.holparb.moviefinder.movies.domain.model.Movie
import com.holparb.moviefinder.movies.presentation.home_screen.components.MoviePosterPicture
import java.time.LocalDate

@Composable
fun MovieVerticalListItem(
    movieVerticalListItemUi: MovieVerticalListItemUi,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
        ) {
            MoviePosterPicture(posterPath = movieVerticalListItemUi.posterPath)
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Text(
                    text = movieVerticalListItemUi.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    Text(
                        text = movieVerticalListItemUi.releaseYear,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.Star,
                        tint = Color.Yellow,
                        contentDescription = "Star icon"
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = movieVerticalListItemUi.rating,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }
                Spacer(Modifier.height(16.dp))
                Text(
                    text = movieVerticalListItemUi.overview,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 5,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview
@Composable
private fun MovieVerticalListItemPreview() {
    val movie = Movie(id = 1, title = "Title", releaseDate = LocalDate.now(), posterPath = "", overview = "This is some Review of the movie here", backdropPath = "")
    MovieVerticalListItem(movieVerticalListItemUi = movie.toMovieVerticalListItemUi(), modifier = Modifier.height(250.dp))
}