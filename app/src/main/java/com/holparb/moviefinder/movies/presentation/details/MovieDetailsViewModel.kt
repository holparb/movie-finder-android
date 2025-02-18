package com.holparb.moviefinder.movies.presentation.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.holparb.moviefinder.core.domain.util.errors.DataError
import com.holparb.moviefinder.core.domain.util.onError
import com.holparb.moviefinder.core.domain.util.onSuccess
import com.holparb.moviefinder.core.navigation.Destination
import com.holparb.moviefinder.movies.domain.repository.MovieRepository
import com.holparb.moviefinder.movies.domain.util.MovieEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val repository: MovieRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private val movieId = savedStateHandle.toRoute<Destination.MovieDetailsDestination>().movieId

    val state = savedStateHandle.getStateFlow("state", MovieDetailsState())
        .onStart {
            loadMovieDetails()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            MovieDetailsState()
        )

    val _events = Channel<MovieEvent>()
    val events = _events.receiveAsFlow()

    private fun loadMovieDetails() {
        savedStateHandle["state"] = savedStateHandle.get<MovieDetailsState>("state")?.copy(
            isLoading = true
        )
        viewModelScope.launch {
            repository.getMovieDetails(movieId)
                .onSuccess {
                    savedStateHandle["state"] = savedStateHandle.get<MovieDetailsState>("state")?.copy(
                        isLoading = false,
                        movieDetailsUi = it.toMovieDetailsUi()
                    )
                }
                .onError { error ->
                    savedStateHandle["state"] = savedStateHandle.get<MovieDetailsState>("state")?.copy(
                        isLoading = false
                    )
                    when(error) {
                        is DataError.Network -> _events.send(MovieEvent.RemoteError(error.networkError))
                        is DataError.Database -> _events.send(MovieEvent.LocalError(error.databaseError))
                    }
                }
        }

    }
}