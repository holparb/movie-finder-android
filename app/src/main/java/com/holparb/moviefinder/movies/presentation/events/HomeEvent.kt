package com.holparb.moviefinder.movies.presentation.events

sealed interface HomeEvent {
    data object LoadPopularMovies: HomeEvent
}