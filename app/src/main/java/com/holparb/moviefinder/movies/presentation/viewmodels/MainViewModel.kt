package com.holparb.moviefinder.movies.presentation.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.holparb.moviefinder.movies.presentation.states.MainScreenState

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