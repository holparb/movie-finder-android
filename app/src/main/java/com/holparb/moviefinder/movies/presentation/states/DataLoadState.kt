package com.holparb.moviefinder.movies.presentation.states

sealed class DataLoadState<out T, out E> {
    data object Loading: DataLoadState<Nothing, Nothing>()
    data class Loaded<T>(val data: T): DataLoadState<T, Nothing>()
    data class Error<E>(val error: E): DataLoadState<Nothing, E>()
}