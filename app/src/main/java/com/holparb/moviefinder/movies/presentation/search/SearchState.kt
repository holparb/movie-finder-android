package com.holparb.moviefinder.movies.presentation.search

import com.holparb.moviefinder.movies.domain.model.Movie

data class SearchState(
    val movies: List<Movie> = emptyList(),
    val isLoading: Boolean = false
)
