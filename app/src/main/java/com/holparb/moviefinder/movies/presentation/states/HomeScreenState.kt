package com.holparb.moviefinder.movies.presentation.states

import com.holparb.moviefinder.movies.domain.model.MovieListItem
import com.holparb.moviefinder.movies.domain.util.MovieError

data class HomeScreenState(
    val movieLists: Map<String, DataLoadState<List<MovieListItem>, MovieError>> = mapOf(
        mainItem to DataLoadState.Loading,
        popularMovies to DataLoadState.Loading,
        topRatedMovies to DataLoadState.Loading,
        upcomingMovies to DataLoadState.Loading
    ),
) {
    companion object {
        val mainItem = "mainItem"
        val popularMovies = "popularMovies"
        val topRatedMovies = "topRatedMovies"
        val upcomingMovies = "upcomingMovies"
    }
}