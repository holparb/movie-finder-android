package com.holparb.moviefinder.movies.presentation.watchlist.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.holparb.moviefinder.movies.presentation.see_more_screen.components.MovieVerticalListItem
import com.holparb.moviefinder.movies.presentation.watchlist.WatchlistState

@Composable
fun Watchlist(
    state: WatchlistState,
    modifier: Modifier = Modifier
) {
    if(state.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .width(64.dp)
                    .align(Alignment.Center),
                color = MaterialTheme.colorScheme.onBackground,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        }
    } else {
        LazyColumn(
            modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(state.movies) { movieVerticalListItemUi ->
                MovieVerticalListItem(
                    modifier = Modifier.height(200.dp),
                    movieVerticalListItemUi = movieVerticalListItemUi
                )
            }
        }
    }

}