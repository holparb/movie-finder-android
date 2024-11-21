package com.holparb.moviefinder.movies.presentation.home_screen

import com.holparb.moviefinder.movies.domain.model.MovieListType

data class HomeScreenState(
    val movieLists: Map<String, MovieListState> = mapOf(
        MAIN_ITEM to MovieListState(movieListType = MovieListType.PopularMovies),
        POPULAR_MOVIES to MovieListState(movieListType = MovieListType.PopularMovies),
        TOP_RATED_MOVIES to MovieListState(movieListType = MovieListType.TopRatedMovies),
        UPCOMING_MOVIES to MovieListState(movieListType = MovieListType.UpcomingMovies)
    ),
) {
    companion object {
        const val MAIN_ITEM = "mainItem"
        const val POPULAR_MOVIES = "popularMovies"
        const val TOP_RATED_MOVIES = "topRatedMovies"
        const val UPCOMING_MOVIES = "upcomingMovies"
    }
}