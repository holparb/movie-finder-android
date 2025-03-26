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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.holparb.moviefinder.R
import com.holparb.moviefinder.core.presentation.components.LoadingShimmerEffect
import com.holparb.moviefinder.movies.domain.model.MovieListType
import com.holparb.moviefinder.movies.presentation.home_screen.MovieListState
import com.holparb.moviefinder.ui.theme.MovieFinderTheme
import java.time.format.DateTimeFormatter

@Composable
fun MainMovieItem(
    state: MovieListState,
    onNavigateToMovieDetails: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                enabled = state.isLoading.not() && state.movieList.isNotEmpty()
            ) {
                onNavigateToMovieDetails(state.movieList.first().id)
            }
    ) {
        if(state.movieList.isNotEmpty()) {
            state.movieList.first().let { mainItem ->
                SubcomposeAsyncImage(
                    model = mainItem.backdropPath,
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
                    loading = {
                        LoadingShimmerEffect(
                            modifier = modifier.fillMaxSize().background(Color.LightGray)
                        )
                    },
                    contentDescription = "movie poster",
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier.fillMaxSize()
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
        MainMovieItem(state = MovieListState(movieListType = MovieListType.PopularMovies), onNavigateToMovieDetails = {}, modifier = Modifier.height(300.dp))
    }
}