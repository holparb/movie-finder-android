package com.holparb.moviefinder.movies.presentation.states

import com.holparb.moviefinder.movies.domain.model.MovieListItem
import com.holparb.moviefinder.movies.domain.util.MovieError

sealed class MovieListState {
    data object Empty: MovieListState()
    data object Loading: MovieListState()
    data class Error(val error: MovieError = MovieError.UnknownError()): MovieListState()
    data class Success(val movies: List<MovieListItem> = emptyList()): MovieListState()
}