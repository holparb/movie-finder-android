package com.holparb.moviefinder.movies.presentation.watchlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.holparb.moviefinder.core.domain.util.LocalEncryptedStorage
import com.holparb.moviefinder.core.domain.util.pagination.MovieListPaginator
import com.holparb.moviefinder.di.MovieListPaginatorFactory
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
    private val movieRepository: MovieRepository,
    private val movieListPaginatorFactory: MovieListPaginatorFactory
): ViewModel() {

    private val movieListPaginator: MovieListPaginator = movieListPaginatorFactory.create(
        onLoadUpdated = { isLoading ->
            _state.update { it.copy(isNewPageLoading = isLoading) }
        },
        onRequest = { page ->
            movieRepository.getWatchlist(
                sessionId = localEncryptedStorage.getSessionId()!!,
                page = page
            )
        },
        onError = { error ->
            _events.send(WatchlistEvent.WatchlistError(error))
        },
        onSuccess = { movies ->
            if(movies.isEmpty()) {
                _state.update {
                    it.copy(isLastPageReached = true)
                }
            } else {
                _state.update { state ->
                    state.copy(
                        movies = state.movies + movies.map { movie ->
                            movie.toMovieVerticalListItemUi()
                        }
                    )
                }
            }
        }
    )

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

    fun onAction(action: WatchlistAction) {
        when(action) {
           is WatchlistAction.LoadWatchlist -> { loadWatchlist() }
        }
    }

    private fun loadWatchlist() {
        viewModelScope.launch {
            movieListPaginator.loadNextPage()
        }
    }
}