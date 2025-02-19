package com.holparb.moviefinder.movies.presentation.watchlist.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.holparb.moviefinder.movies.presentation.watchlist.WatchlistState

@Composable
fun WatchlistBody(
    state: WatchlistState,
    navigateToLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.padding(bottom = 16.dp)
    ) {
        if(state.isUserLoggedIn.not()) {
            WatchlistNotLoggedIn(navigateToLogin = navigateToLogin)
        } else {
            Watchlist(
                state = state
            )
        }
    }
}