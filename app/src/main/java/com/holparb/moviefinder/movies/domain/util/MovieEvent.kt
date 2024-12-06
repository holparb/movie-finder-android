package com.holparb.moviefinder.movies.domain.util

import com.holparb.moviefinder.core.domain.util.DatabaseError
import com.holparb.moviefinder.core.domain.util.NetworkError

sealed interface MovieEvent {
    data class RemoteError(val error: NetworkError): MovieEvent
    data class LocalError(val error: DatabaseError): MovieEvent
}