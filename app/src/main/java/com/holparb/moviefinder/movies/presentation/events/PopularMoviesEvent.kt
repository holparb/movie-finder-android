package com.holparb.moviefinder.movies.presentation.events

sealed interface PopularMoviesEvent {
    data object LoadPopularMovies: PopularMoviesEvent
}