package com.holparb.moviefinder.movies.presentation.search.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.holparb.moviefinder.movies.presentation.details.components.previewMovie
import com.holparb.moviefinder.movies.presentation.home_screen.components.MoviePosterPicture
import com.holparb.moviefinder.movies.presentation.search.SearchAction
import com.holparb.moviefinder.movies.presentation.search.SearchState
import com.holparb.moviefinder.ui.theme.MovieFinderTheme

@Composable
fun SearchResultList(
    state: SearchState,
    onAction: (SearchAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val gridState = rememberLazyGridState()
    val isScrolledToEnd by remember {
        derivedStateOf {
            val lastVisibleItem = gridState.layoutInfo.visibleItemsInfo.lastOrNull() ?:
            return@derivedStateOf false
            gridState.layoutInfo.totalItemsCount - lastVisibleItem.index <= 2
        }
    }
    LaunchedEffect(isScrolledToEnd) {
        if(isScrolledToEnd && !state.isLastPageReached && !state.isNewPageLoading) {
            onAction(SearchAction.LoadMorePages)
        }
    }

    LazyVerticalGrid(
        state = gridState,
        columns = GridCells.Adaptive(120.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxSize()
    ) {
        items(state.movies) { movie ->
            MoviePosterPicture(
                posterPath = movie.posterPath
            )
        }
        if(state.isNewPageLoading) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(top = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(25.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchResultListPreview() {
    MovieFinderTheme {
        SearchResultList(
            state = SearchState(movies = listOf(
                previewMovie,
                previewMovie,
                previewMovie,
                previewMovie,
                previewMovie
            )),
           onAction = {}
        )
    }
}