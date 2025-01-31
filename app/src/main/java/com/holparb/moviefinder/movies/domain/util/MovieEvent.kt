package com.holparb.moviefinder.movies.domain.util

import com.holparb.moviefinder.core.domain.util.errors.DatabaseError
import com.holparb.moviefinder.core.domain.util.errors.NetworkError

sealed interface MovieEvent {
    data class RemoteError(val error: NetworkError): MovieEvent
    data class LocalError(val error: DatabaseError): MovieEvent
}