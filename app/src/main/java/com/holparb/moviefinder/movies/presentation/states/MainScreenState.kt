package com.holparb.moviefinder.movies.presentation.states

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MainScreenState(
    val selectedIndex: Int = 1
): Parcelable
