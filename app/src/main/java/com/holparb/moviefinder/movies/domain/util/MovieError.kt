package com.holparb.moviefinder.movies.domain.util

sealed class MovieError() {
    data class NetworkError(val message: String): MovieError()
    data class LocalDatabaseError(val message: String): MovieError()
    data class UnknownError(val message: String = "Unknown error occured"): MovieError()
}