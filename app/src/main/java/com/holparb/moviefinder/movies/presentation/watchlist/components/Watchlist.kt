package com.holparb.moviefinder.movies.presentation.watchlist.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.holparb.moviefinder.movies.presentation.see_more_screen.components.MovieVerticalListItem
import com.holparb.moviefinder.movies.presentation.watchlist.WatchlistAction
import com.holparb.moviefinder.movies.presentation.watchlist.WatchlistState

@Composable
fun Watchlist(
    state: WatchlistState,
    onAction: (WatchlistAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val isScrolledToEnd by remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
            lastVisibleItem?.index != 0 && lastVisibleItem?.index == listState.layoutInfo.totalItemsCount - 1
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
                movieVerticalListItemUi = state.movies[index]
            )
        }
    }
}