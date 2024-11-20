package com.holparb.moviefinder.movies.presentation.home_screen

import com.holparb.moviefinder.movies.domain.model.Movie

data class MovieListState(
    val isLoading: Boolean = false,
    val movieList: List<Movie> = emptyList(),
    val loadEvent: MovieListLoadEvent = MovieListLoadEvent.Unknown
)
