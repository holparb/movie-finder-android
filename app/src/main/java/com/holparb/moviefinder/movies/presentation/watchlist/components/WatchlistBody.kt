package com.holparb.moviefinder.movies.presentation.watchlist.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.holparb.moviefinder.movies.presentation.watchlist.WatchlistState

@Composable
fun WatchlistBody(
    state: WatchlistState,
    navigateToLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    if(state.isUserLoggedIn.not()) {
        WatchlistNotLoggedIn(navigateToLogin = navigateToLogin)
    } else {
        Watchlist(state = state)
    }
}