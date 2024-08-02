package com.holparb.moviefinder.movies.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.holparb.moviefinder.movies.domain.repository.MovieRepository
import com.holparb.moviefinder.movies.domain.util.Resource
import com.holparb.moviefinder.movies.presentation.events.HomeEvent
import com.holparb.moviefinder.movies.presentation.states.MainItemState
import com.holparb.moviefinder.movies.presentation.states.MainItemStatus
import com.holparb.moviefinder.movies.presentation.states.MovieListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PopularMoviesViewModel @Inject constructor(
    private val repository: MovieRepository
): ViewModel() {

    private val _popularMoviesState = MutableStateFlow(MovieListState())
    val popularMoviesState = _popularMoviesState.asStateFlow()

    val mainItemState = _popularMoviesState.map { popularMoviesState ->
        val mainItemSate = MainItemState(
            status = resolveMainItemState(popularMoviesState),
            mainItem = if (popularMoviesState.movies.isNotEmpty()) popularMoviesState.movies.first() else null
        )
        mainItemSate
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), MainItemState())

    private fun resolveMainItemState(popularMoviesState: MovieListState): MainItemStatus {
        if(popularMoviesState.loading) {
            return MainItemStatus.Loading
        }
        if(popularMoviesState.errorMessage.isNullOrEmpty() && popularMoviesState.movies.isNotEmpty()) {
            return MainItemStatus.Loaded
        }
        return MainItemStatus.Error
    }

    fun onEvent(event: HomeEvent) {
        when(event) {
            HomeEvent.LoadPopularMovies -> {
                viewModelScope.launch {
                    _popularMoviesState.update {
                        it.copy(
                            loading = true
                        )
                    }
                    when(val result = repository.getPopularMovies()) {
                        is Resource.Error -> {
                            _popularMoviesState.update {
                                it.copy(
                                    loading = false,
                                    errorMessage = result.error.message,
                                    movies = emptyList()
                                )
                            }
                        }
                        is Resource.Success -> {
                            _popularMoviesState.update {
                                it.copy(
                                    loading = false,
                                    errorMessage = null,
                                    movies = result.data
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}