package com.holparb.moviefinder.movies.presentation.states

import com.holparb.moviefinder.movies.domain.model.MovieListItem
import com.holparb.moviefinder.movies.domain.util.MovieError
import com.holparb.moviefinder.movies.presentation.events.MovieListLoadEvent

data class MovieListState(
    val movieList: DataLoadState<List<MovieListItem>, MovieError> = DataLoadState.Loading,
    val loadEvent: MovieListLoadEvent = MovieListLoadEvent.Unknown
)
