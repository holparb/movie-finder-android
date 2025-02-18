package com.holparb.moviefinder.movies.presentation.watchlist.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.holparb.moviefinder.movies.presentation.watchlist.WatchlistState

@Composable
fun WatchlistBody(
    state: WatchlistState,
    isUserLoggedIn: Boolean,
    navigateToLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    if(isUserLoggedIn.not()) {
        WatchlistNotLoggedIn(navigateToLogin = navigateToLogin)
    } else {
        Watchlist(state = state)
    }
}