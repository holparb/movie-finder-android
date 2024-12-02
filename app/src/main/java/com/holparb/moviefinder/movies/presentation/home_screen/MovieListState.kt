package com.holparb.moviefinder.movies.presentation.home_screen

import com.holparb.moviefinder.movies.domain.model.Movie
import com.holparb.moviefinder.movies.domain.model.MovieListType

data class MovieListState(
    val isLoading: Boolean = false,
    val movieList: List<Movie> = emptyList(),
    val movieListType: MovieListType
)
