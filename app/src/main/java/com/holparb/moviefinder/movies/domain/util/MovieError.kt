package com.holparb.moviefinder.movies.domain.util

sealed class MovieError {
    data class NetworkError(val message: String): MovieError()
}