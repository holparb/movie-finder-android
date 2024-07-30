package com.holparb.moviefinder.movies.presentation.states

import com.holparb.moviefinder.movies.domain.model.MovieListItem

data class MainItemState(
    val loading: Boolean = false,
    val error: Boolean = false,
    val mainItem: MovieListItem? = null,
)