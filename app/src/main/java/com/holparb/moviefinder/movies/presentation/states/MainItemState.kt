package com.holparb.moviefinder.movies.presentation.states

import com.holparb.moviefinder.movies.domain.model.MovieListItem
import com.holparb.moviefinder.movies.domain.util.MovieError


sealed class MainItemState {
    data object Loading: MainItemState()
    data class Error(val error: MovieError = MovieError.UnknownError()): MainItemState()
    data class Success(val mainItem: MovieListItem? = null): MainItemState()
}