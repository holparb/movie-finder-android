package com.holparb.moviefinder.movies.presentation.watchlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.holparb.moviefinder.auth.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WatchlistViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

    private val _loggedInStatusChannel = Channel<Boolean>()
    val loggedInStatusChannel = _loggedInStatusChannel.receiveAsFlow()

    init {
        checkIfUserIsLoggedIn()
    }

    private fun checkIfUserIsLoggedIn() {
        viewModelScope.launch {
            _loggedInStatusChannel.send(authRepository.isUserLoggedIn())
        }
    }
}