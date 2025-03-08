package com.holparb.moviefinder.movies.presentation.watchlist

sealed interface WatchlistAction {
    object LoadWatchlist: WatchlistAction
}