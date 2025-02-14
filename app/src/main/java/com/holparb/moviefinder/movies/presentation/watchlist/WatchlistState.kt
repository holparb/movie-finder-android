package com.holparb.moviefinder.movies.presentation.watchlist

import com.holparb.moviefinder.movies.presentation.see_more_screen.components.MovieVerticalListItemUi

data class WatchlistState(
    val movies: List<MovieVerticalListItemUi> = emptyList(),
    val isLoading: Boolean = false
)
