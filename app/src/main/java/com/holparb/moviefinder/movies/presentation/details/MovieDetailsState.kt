package com.holparb.moviefinder.movies.presentation.details

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieDetailsState(
    val movieDetailsUi: MovieDetailsUi = MovieDetailsUi(),
    val isLoading: Boolean = false
): Parcelable
