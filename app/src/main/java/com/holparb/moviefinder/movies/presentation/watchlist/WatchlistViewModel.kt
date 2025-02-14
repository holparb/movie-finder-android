package com.holparb.moviefinder.movies.presentation.watchlist

import androidx.lifecycle.ViewModel
import com.holparb.moviefinder.core.domain.util.LocalEncryptedStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class WatchlistViewModel @Inject constructor(
    private val localEncryptedStorage: LocalEncryptedStorage
): ViewModel() {

    private val _state = MutableStateFlow(WatchlistState())
    val state = _state.asStateFlow()

    init {
        val isUserLoggedIn = localEncryptedStorage.getSessionId() != null
        _state.update { it.copy(isUserLoggedIn = isUserLoggedIn) }
    }

}