package com.holparb.moviefinder.movies.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
enum class MovieListType : Parcelable {
    PopularMovies, TopRatedMovies, UpcomingMovies
}