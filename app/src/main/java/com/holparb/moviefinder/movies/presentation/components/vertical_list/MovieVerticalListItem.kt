package com.holparb.moviefinder.movies.presentation.components.vertical_list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.holparb.moviefinder.movies.domain.model.MovieListItem
import com.holparb.moviefinder.movies.presentation.components.MoviePosterPicture
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun MovieVerticalListItem(
    movieListItem: MovieListItem,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            MoviePosterPicture(movie = movieListItem)
            Column() {
                Text(
                   text = movieListItem.title,
                    style = MaterialTheme.typography.titleMedium
                )
                movieListItem.releaseDate?.let { releaseDate ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = releaseDate.format(DateTimeFormatter.ofPattern("yyyy")),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Spacer(Modifier.height(16.dp))
                Text(
                    text = movieListItem.overview,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Preview
@Composable
private fun MovieVerticalListItemPreview() {
    val movie = MovieListItem(id = 1, title = "Title", releaseDate = LocalDate.now(), posterPath = "", overview = "This is some Review of the movie here", backdropPath = "")
    MovieVerticalListItem(movieListItem = movie, modifier = Modifier.height(250.dp))
}