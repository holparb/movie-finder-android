package com.holparb.moviefinder.movies.presentation.home_screen.components

import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.holparb.moviefinder.core.presentation.util.TestTags
import com.holparb.moviefinder.movies.domain.model.MovieListType
import com.holparb.moviefinder.movies.presentation.home_screen.MovieListState

@Composable
fun MovieHorizontalList(
    state: MovieListState,
    title: String,
    onNavigateToMovieDetails: (Int) -> Unit,
    onNavigateToSeeMoreScreen: (String, MovieListType) -> Unit,
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
                onNavigateToSeeMoreScreen(title, state.movieListType)
            }
        )
        Spacer(modifier = Modifier.height(12.dp))
        if(state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .width(64.dp)
                    .align(Alignment.CenterHorizontally)
                    .testTag(TestTags.MOVIE_HORIZONTAL_LIST_LOADING),
                color = MaterialTheme.colorScheme.onBackground,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        } else {
            LazyRow(
                modifier = Modifier.testTag(TestTags.MOVIE_HORIZONTAL_LIST_ITEMS),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(state.movieList) { movie ->
                    MoviePosterPicture(
                        movie = movie,
                        modifier = Modifier
                            .width(120.dp)
                            .height(180.dp)
                            .clickable {
                                onNavigateToMovieDetails(movie.id)
                            }
                    )
                }
            }
        }
    }
}

class ListStateParameterProvider: PreviewParameterProvider<MovieListState> {
    override val values: Sequence<MovieListState>
        get() = sequenceOf(
            MovieListState(movieListType = MovieListType.PopularMovies),
            MovieListState(movieListType = MovieListType.PopularMovies, isLoading = false),
        )
}

@Preview
@Composable
private fun MovieHorizontalListPreview(
    @PreviewParameter(provider = ListStateParameterProvider::class) state: MovieListState
) {
    MovieHorizontalList(state = state, modifier = Modifier.height(250.dp), title = "Title", onNavigateToMovieDetails = {}, onNavigateToSeeMoreScreen = { _, _ -> })
}