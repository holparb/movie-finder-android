package com.holparb.moviefinder.movies.presentation.watchlist.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.holparb.moviefinder.movies.presentation.see_more_screen.components.MovieVerticalListItem
import com.holparb.moviefinder.movies.presentation.watchlist.WatchlistAction
import com.holparb.moviefinder.movies.presentation.watchlist.WatchlistState

@Composable
fun Watchlist(
    state: WatchlistState,
    onAction: (WatchlistAction) -> Unit,
    navigateToDetails: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val isScrolledToEnd by remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull() ?:
                return@derivedStateOf false
            listState.layoutInfo.totalItemsCount - lastVisibleItem.index <= 2
        }
    }
    LaunchedEffect(isScrolledToEnd) {
        if(isScrolledToEnd && !state.isLastPageReached && !state.isNewPageLoading) {
            onAction(WatchlistAction.LoadWatchlist)
        }
    }

    LazyColumn(
        modifier.fillMaxSize(),
        state = listState,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(state.movies.size) { index ->
            MovieVerticalListItem(
                modifier = Modifier.height(200.dp),
                navigateToDetails = navigateToDetails,
                movieVerticalListItemUi = state.movies[index]
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