package com.holparb.moviefinder.movies.presentation.home_screen

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
sealed class MovieListLoadEvent : Parcelable {
    @Serializable
    data object LoadPopularMovies: MovieListLoadEvent()
    @Serializable
    data object LoadTopRatedMovies: MovieListLoadEvent()
    @Serializable
    data object LoadUpcomingMovies: MovieListLoadEvent()
    @Serializable
    data object Unknown: MovieListLoadEvent()
}