package com.holparb.moviefinder.movies.presentation.home_screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.holparb.moviefinder.R
import com.holparb.moviefinder.movies.domain.model.MovieListType
import com.holparb.moviefinder.movies.presentation.home_screen.MovieListState
import com.holparb.moviefinder.ui.theme.MovieFinderTheme
import java.time.format.DateTimeFormatter

@Composable
fun MainMovieItem(
    state: MovieListState,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                enabled = state.isLoading.not() && state.movieList.isNotEmpty()
            ) {

            }
    ) {
        if(state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .width(64.dp)
                    .align(Alignment.Center),
                color = MaterialTheme.colorScheme.onBackground,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        }
        else if (state.movieList.isNotEmpty()){
            state.movieList.first().let { mainItem ->
                SubcomposeAsyncImage(
                    model = mainItem.backdropPath,
                    contentDescription = "movie poster",
                    contentScale = ContentScale.FillHeight,
                    modifier = modifier
                        .fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black),
                                startY = 500f
                            )
                        )
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .align(Alignment.BottomStart)
                ) {
                    Text(
                        text = mainItem.title ,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if(mainItem.releaseDate != null) mainItem.releaseDate.format(
                            DateTimeFormatter.ofPattern("MMM dd, yyyy")) else "",
                    )
                }
            }
        } else {
            Image(
                painter = painterResource(id = R.drawable.backdrop_path_error),
                contentDescription = null,
                contentScale = ContentScale.FillHeight,
                modifier = modifier
                    .fillMaxSize()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MainMovieItemPreview() {
    MovieFinderTheme {
        MainMovieItem(state = MovieListState(movieListType = MovieListType.PopularMovies), modifier = Modifier.height(300.dp))
    }
}