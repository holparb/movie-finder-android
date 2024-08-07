package com.holparb.moviefinder.movies.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.holparb.moviefinder.movies.domain.repository.MovieRepository
import com.holparb.moviefinder.movies.domain.util.Resource
import com.holparb.moviefinder.movies.presentation.events.PopularMoviesEvent
import com.holparb.moviefinder.movies.presentation.states.MainItemState
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

    private val _popularMoviesState = MutableStateFlow<MovieListState>(MovieListState.Loading)
    val popularMoviesState = _popularMoviesState.asStateFlow()

    val mainItemState = _popularMoviesState.map { popularMoviesState ->
        resolveMainItemState(popularMoviesState)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), MainItemState.Loading)

    private fun resolveMainItemState(popularMoviesState: MovieListState): MainItemState {
        return when(popularMoviesState) {
            is MovieListState.Error -> MainItemState.Error(error = popularMoviesState.error)
            MovieListState.Loading -> MainItemState.Loading
            is MovieListState.Success -> MainItemState.Success(mainItem = popularMoviesState.movies.first())
            MovieListState.Empty -> MainItemState.Success(mainItem = null)
        }
    }


    fun onEvent(event: PopularMoviesEvent) {
        when(event) {
            PopularMoviesEvent.LoadPopularMovies -> {
                viewModelScope.launch {
                    _popularMoviesState.update {
                        MovieListState.Loading
                    }
                    when(val result = repository.getPopularMovies()) {
                        is Resource.Error -> {
                            _popularMoviesState.update {
                                MovieListState.Error(result.error)
                            }
                        }
                        is Resource.Success -> {
                            _popularMoviesState.update {
                                MovieListState.Success(result.data)
                            }
                        }
                    }
                }
            }
        }
    }
}