package com.holparb.moviefinder.movies.presentation.watchlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.holparb.moviefinder.core.domain.util.LocalEncryptedStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class WatchlistViewModel @Inject constructor(
    private val localEncryptedStorage: LocalEncryptedStorage
): ViewModel() {

    private val _state = MutableStateFlow(WatchlistState())
    val state = _state.asStateFlow()

    private val _isUserLoggedIn = MutableStateFlow(false)
    val isUserLoggedIn = _isUserLoggedIn.asStateFlow()
        .onStart {
            _isUserLoggedIn.update { localEncryptedStorage.getSessionId() != null }
        }.stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            false
        )
}