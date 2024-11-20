package com.holparb.moviefinder.movies.presentation.main_screen

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MainScreenState(
    val selectedIndex: Int = 1
): Parcelable
