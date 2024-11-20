package com.holparb.moviefinder.movies.presentation.main_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class MainViewModel(
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    val state = savedStateHandle.getStateFlow("state", MainScreenState())

    fun changeNavigationSelectedIndex(index: Int) {
        savedStateHandle["state"] = savedStateHandle.get<MainScreenState>("state")?.copy(
            selectedIndex = index
        )
    }
}