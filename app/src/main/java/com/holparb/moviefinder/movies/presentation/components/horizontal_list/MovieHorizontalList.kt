package com.holparb.moviefinder.movies.presentation.components.horizontal_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.holparb.moviefinder.movies.presentation.states.MovieListState

@Composable
fun MovieHorizontalList(
    state: MovieListState,
    title: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp)
    ) {
        SectionHeader(
            title = title, onClick = {}
        )
        Spacer(modifier = Modifier.height(12.dp))
        when(state) {
            MovieListState.Empty -> {
                Text(
                    text = "No items in the list",
                    style = TextStyle(
                        fontSize = 30.sp,
                        color = MaterialTheme.colorScheme.onSecondary
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            is MovieListState.Error -> {
                Text(
                    text = "Items failed to load, please check your network connection and try again",
                    style = TextStyle(
                        fontSize = 30.sp,
                        color = MaterialTheme.colorScheme.error
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            MovieListState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .width(64.dp)
                        .align(Alignment.CenterHorizontally),
                    color = MaterialTheme.colorScheme.onBackground,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }
            is MovieListState.Success -> {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(state.movies) { item ->
                        MovieHorizontalListItem(
                            movie = item,
                            modifier = Modifier
                                .width(120.dp)
                                .height(180.dp)
                        )
                    }
                }
            }
        }
    }
}

class StateParameterProvider: PreviewParameterProvider<MovieListState> {
    override val values: Sequence<MovieListState>
        get() = sequenceOf(
            MovieListState.Empty, MovieListState.Loading, MovieListState.Error(), MovieListState.Success(emptyList()))
}

@Preview
@Composable
private fun MovieHorizontalListPreview(
    @PreviewParameter(provider = StateParameterProvider::class) state: MovieListState
) {
    MovieHorizontalList(state = state, modifier = Modifier.height(250.dp), title = "Title")
}