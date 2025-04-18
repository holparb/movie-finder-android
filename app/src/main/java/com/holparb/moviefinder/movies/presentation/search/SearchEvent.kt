package com.holparb.moviefinder.movies.presentation.search

import com.holparb.moviefinder.core.domain.util.errors.NetworkError

sealed interface SearchEvent {
    data class searchError(val networkError: NetworkError): SearchEvent
}