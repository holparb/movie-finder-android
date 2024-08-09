package com.holparb.moviefinder.movies.presentation.events

sealed interface HomeScreenEvent {
    data object LoadPopularMovies: HomeScreenEvent
    data object LoadTopRatedMovies: HomeScreenEvent
    data object LoadUpcomingMovies: HomeScreenEvent
    data object LoadEverything: HomeScreenEvent
}