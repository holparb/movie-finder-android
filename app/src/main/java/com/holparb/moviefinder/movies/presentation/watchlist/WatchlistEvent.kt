package com.holparb.moviefinder.movies.presentation.watchlist

import com.holparb.moviefinder.core.domain.util.errors.DataError

sealed interface WatchlistEvent {
    data class WatchlistError(val error: DataError): WatchlistEvent
}
