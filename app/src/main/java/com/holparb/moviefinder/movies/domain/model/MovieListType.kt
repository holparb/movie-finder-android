package com.holparb.moviefinder.movies.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class MovieListType {
    PopularMovies, TopRatedMovies, UpcomingMovies
}