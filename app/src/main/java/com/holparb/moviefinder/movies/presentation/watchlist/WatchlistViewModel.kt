package com.holparb.moviefinder.movies.presentation.watchlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.holparb.moviefinder.core.domain.util.LocalEncryptedStorage
import com.holparb.moviefinder.core.domain.util.onError
import com.holparb.moviefinder.core.domain.util.onSuccess
import com.holparb.moviefinder.movies.domain.repository.MovieRepository
import com.holparb.moviefinder.movies.presentation.see_more_screen.components.toMovieVerticalListItemUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WatchlistViewModel @Inject constructor(
    private val localEncryptedStorage: LocalEncryptedStorage,
    private val movieRepository: MovieRepository
): ViewModel() {

    private val _state = MutableStateFlow(WatchlistState())
    val state = _state
        .onStart {
            val isUserLoggedIn = localEncryptedStorage.getSessionId() != null
            _state.update {
                it.copy(
                    isUserLoggedIn = isUserLoggedIn
                )
            }
            if(isUserLoggedIn) {
                loadWatchlist()
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            WatchlistState()
        )

    private val _events = Channel<WatchlistEvent>()
    val events = _events.receiveAsFlow()

    private fun loadWatchlist() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            movieRepository.getWatchlist(localEncryptedStorage.getSessionId()!!)
                .onSuccess { movies ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            movies = movies.map { movie ->
                                movie.toMovieVerticalListItemUi()
                            }
                        )
                    }
                }
                .onError { dataError ->
                    _state.update { it.copy(isLoading = false) }
                    _events.send(WatchlistEvent.WatchlistError(dataError))
                }
        }
    }
}