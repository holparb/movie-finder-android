package com.holparb.moviefinder.movies.presentation.home_screen.components

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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.holparb.moviefinder.core.domain.util.NetworkError
import com.holparb.moviefinder.core.presentation.util.TestTags
import com.holparb.moviefinder.movies.presentation.navigation.SeeMoreScreenComposable
import com.holparb.moviefinder.movies.presentation.home_screen.MovieListState
import com.holparb.moviefinder.movies.presentation.states.DataLoadState

@Composable
fun MovieHorizontalList(
    state: MovieListState,
    title: String,
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        SectionHeader(
            title = title,
            onClick = {
                navController.navigate(SeeMoreScreenComposable(title = title, loadEvent = state.loadEvent))
            }
        )
        Spacer(modifier = Modifier.height(12.dp))
        when(state.movieList) {
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
                        .align(Alignment.CenterHorizontally)
                        .testTag(TestTags.MOVIE_HORIZONTAL_LIST_LOADING),
                    color = MaterialTheme.colorScheme.onBackground,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }
            is DataLoadState.Loaded -> {
                LazyRow(
                    modifier = Modifier.testTag(TestTags.MOVIE_HORIZONTAL_LIST_ITEMS),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(state.movieList.data) { item ->
                        MoviePosterPicture(
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

class ListStateParameterProvider: PreviewParameterProvider<MovieListState> {
    override val values: Sequence<MovieListState>
        get() = sequenceOf(
            MovieListState(movieList = DataLoadState.Loading),
            MovieListState(movieList = DataLoadState.Error(NetworkError.UNKNOWN)),
            MovieListState(movieList = DataLoadState.Loaded(emptyList()))
        )
}

@Preview
@Composable
private fun MovieHorizontalListPreview(
    @PreviewParameter(provider = StateParameterProvider::class) state: MovieListState
) {
    MovieHorizontalList(state = state, modifier = Modifier.height(250.dp), title = "Title", navController = rememberNavController())
}