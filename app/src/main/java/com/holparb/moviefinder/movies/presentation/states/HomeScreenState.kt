package com.holparb.moviefinder.movies.presentation.states

import com.holparb.moviefinder.movies.presentation.events.MovieListLoadEvent

data class HomeScreenState(
    val movieLists: Map<String, MovieListState> = mapOf(
        MAIN_ITEM to MovieListState(movieList = DataLoadState.Loading, loadEvent = MovieListLoadEvent.LoadPopularMovies),
        POPULAR_MOVIES to MovieListState(movieList = DataLoadState.Loading, loadEvent = MovieListLoadEvent.LoadPopularMovies),
        TOP_RATED_MOVIES to MovieListState(movieList = DataLoadState.Loading, loadEvent = MovieListLoadEvent.LoadTopRatedMovies),
        UPCOMING_MOVIES to MovieListState(movieList = DataLoadState.Loading, loadEvent = MovieListLoadEvent.LoadUpcomingMovies)
    ),
) {
    companion object {
        const val MAIN_ITEM = "mainItem"
        const val POPULAR_MOVIES = "popularMovies"
        const val TOP_RATED_MOVIES = "topRatedMovies"
        const val UPCOMING_MOVIES = "upcomingMovies"
    }
}