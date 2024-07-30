package com.holparb.moviefinder.movies.presentation.states

import com.holparb.moviefinder.movies.domain.model.MovieListItem

data class MovieListState(
    val loading: Boolean = false,
    val movies: List<MovieListItem> = emptyList(),
    val errorMessage: String? = null
)