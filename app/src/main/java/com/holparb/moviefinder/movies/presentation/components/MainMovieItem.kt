package com.holparb.moviefinder.movies.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.holparb.moviefinder.movies.domain.model.MovieListItem
import com.holparb.moviefinder.movies.domain.util.MovieError
import com.holparb.moviefinder.movies.presentation.states.DataLoadState
import com.holparb.moviefinder.ui.theme.MovieFinderTheme
import java.time.format.DateTimeFormatter

@Composable
fun MainMovieItem(
    dataLoadState: DataLoadState<List<MovieListItem>, MovieError>?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        when(dataLoadState) {
            is DataLoadState.Error -> {
                Text(
                    text = "Item failed to load, please check your network connection and try again",
                    style = TextStyle(
                        fontSize = 30.sp,
                        color = MaterialTheme.colorScheme.error
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            DataLoadState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .width(64.dp)
                        .align(Alignment.Center),
                    color = MaterialTheme.colorScheme.onBackground,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }
            is DataLoadState.Loaded -> {
                AsyncImage(
                    model = dataLoadState.data.first().backdropPath,
                    error = rememberVectorPainter(image = Icons.Sharp.Warning),
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
                dataLoadState.data.first().let { mainItem ->
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
            }

            null -> {
                Text(
                    text = "Item failed to load, please check your network connection and try again",
                    style = TextStyle(
                        fontSize = 30.sp,
                        color = MaterialTheme.colorScheme.error
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

class StateParameterProvider: PreviewParameterProvider<DataLoadState<List<MovieListItem>, MovieError>> {
    override val values: Sequence<DataLoadState<List<MovieListItem>, MovieError>>
        get() = sequenceOf(
            DataLoadState.Loading,
            DataLoadState.Error(MovieError.UnknownError()),
            DataLoadState.Loaded(emptyList())
        )
}

@Preview(showBackground = true)
@Composable
private fun MainMovieItemPreview(
    @PreviewParameter(provider = StateParameterProvider::class) state: DataLoadState<List<MovieListItem>, MovieError>
) {
    MovieFinderTheme {
        MainMovieItem(dataLoadState = state, modifier = Modifier.height(300.dp))
    }
}