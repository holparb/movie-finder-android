package com.holparb.moviefinder.movies.presentation.home_screen

import com.holparb.moviefinder.core.domain.util.DatabaseError
import com.holparb.moviefinder.core.domain.util.NetworkError

sealed interface MovieListEvent {
    data class RemoteError(val error: NetworkError): MovieListEvent
    data class LocalError(val error: DatabaseError): MovieListEvent
}