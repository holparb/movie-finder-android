package com.holparb.moviefinder.movies.presentation.watchlist.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
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
    val scrollState = rememberLazyListState()
    val isScrolledToEnd by remember {
        derivedStateOf {
            val layoutInfo = scrollState.layoutInfo
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
                ?: return@derivedStateOf true
            lastVisibleItem.index == layoutInfo.totalItemsCount - 1
        }
    }
    LaunchedEffect(isScrolledToEnd) {
        if(isScrolledToEnd && !state.isLastPageReached) {
            onAction(WatchlistAction.LoadWatchlist)
        }
    }

    LazyColumn(
        modifier.fillMaxSize(),
        state = scrollState,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(state.movies.size) { index ->
            MovieVerticalListItem(
                modifier = Modifier.height(200.dp),
                movieVerticalListItemUi = state.movies[index]
            )
        }
        item {
            if(state.isNewPageLoading) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}