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
import com.holparb.moviefinder.movies.domain.model.MovieListItem
import com.holparb.moviefinder.movies.domain.util.MovieError
import com.holparb.moviefinder.movies.presentation.components.MoviePosterPicture
import com.holparb.moviefinder.movies.presentation.states.DataLoadState

@Composable
fun MovieHorizontalList(
    state: DataLoadState<List<MovieListItem>, MovieError>?,
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
            is DataLoadState.Error -> {
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
            DataLoadState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .width(64.dp)
                        .align(Alignment.CenterHorizontally),
                    color = MaterialTheme.colorScheme.onBackground,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }
            is DataLoadState.Loaded -> {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(state.data) { item ->
                        MoviePosterPicture(
                            movie = item,
                            modifier = Modifier
                                .width(120.dp)
                                .height(180.dp)
                        )
                    }
                }
            }

            null -> TODO()
        }
    }
}

class StateParameterProvider: PreviewParameterProvider<DataLoadState<List<MovieListItem>, MovieError>> {
    override val values: Sequence<DataLoadState<List<MovieListItem>, MovieError>>
        get() = sequenceOf(
            DataLoadState.Loading, DataLoadState.Error(MovieError.UnknownError()), DataLoadState.Loaded(emptyList()))
}

@Preview
@Composable
private fun MovieHorizontalListPreview(
    @PreviewParameter(provider = StateParameterProvider::class) state: DataLoadState<List<MovieListItem>, MovieError>
) {
    MovieHorizontalList(state = state, modifier = Modifier.height(250.dp), title = "Title")
}