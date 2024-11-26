package com.holparb.moviefinder.movies.presentation.home_screen

import com.holparb.moviefinder.core.domain.util.NetworkError

sealed interface MovieListEvent {
    data class Error(val error: NetworkError): MovieListEvent
}