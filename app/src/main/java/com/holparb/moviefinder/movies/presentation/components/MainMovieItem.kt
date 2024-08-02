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
import com.holparb.moviefinder.movies.presentation.states.MainItemState
import com.holparb.moviefinder.movies.presentation.states.MainItemStatus
import com.holparb.moviefinder.ui.theme.MovieFinderTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun MainMovieItem(
    state: MainItemState,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        when(state.status) {
            MainItemStatus.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .width(64.dp)
                        .align(Alignment.Center),
                    color = MaterialTheme.colorScheme.onBackground,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }
            MainItemStatus.Error -> {
                Text(
                    text = "Item failed to load, please check your network connection and try again",
                    style = TextStyle(
                        fontSize = 30.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            MainItemStatus.Loaded -> {
                AsyncImage(
                    model = state.mainItem?.backdropPath,
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
                state.mainItem?.let { mainItem ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .align(Alignment.BottomStart)
                    ) {
                        Text(
                            text = state.mainItem.title ,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if(state.mainItem.releaseDate != null) state.mainItem.releaseDate.format(
                                DateTimeFormatter.ofPattern("MMM dd, yyyy")) else "",
                        )
                    }
                }
            }
        }
    }
}

class StatusParameterProvider: PreviewParameterProvider<MainItemStatus> {
    override val values: Sequence<MainItemStatus>
        get() = sequenceOf(MainItemStatus.Loading, MainItemStatus.Error, MainItemStatus.Loaded)
}

@Preview(showBackground = true)
@Composable
private fun MainMovieItemPreview(
    @PreviewParameter(provider = StatusParameterProvider::class) status: MainItemStatus
) {
    val state = MainItemState(
        status = status,
        mainItem = MovieListItem(
            id = 1,
            title = "Title",
            releaseDate = LocalDate.now(),
            backdropPath = "https://image.tmdb.org/t/p/w500/A4JG9mkAjOQ3XNJy2oji1Jr224R.jpg",
            posterPath = "https://image.tmdb.org/t/p/w500/8cdWjvZQUExUUTzyp4t6EDMubfO.jpg"
        )
    )
    MovieFinderTheme {
        MainMovieItem(state = state, modifier = Modifier.height(300.dp))
    }
}