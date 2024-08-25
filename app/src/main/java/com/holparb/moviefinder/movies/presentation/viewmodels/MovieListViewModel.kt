package com.holparb.moviefinder.movies.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.holparb.moviefinder.movies.domain.model.MovieListItem
import com.holparb.moviefinder.movies.domain.repository.MovieRepository
import com.holparb.moviefinder.movies.domain.util.MovieError
import com.holparb.moviefinder.movies.domain.util.Resource
import com.holparb.moviefinder.movies.presentation.events.MovieListLoadEvent
import com.holparb.moviefinder.movies.presentation.states.DataLoadState
import com.holparb.moviefinder.movies.presentation.states.MovieListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val repository: MovieRepository
): ViewModel() {

    private val _state = MutableStateFlow(MovieListState())
    val state = _state.asStateFlow()

    private fun updateMovieListStateBasedOnResult(
        result: Resource<List<MovieListItem>, MovieError>
    ) {
        when(result) {
            is Resource.Error -> {
                _state.update {
                    it.copy(
                        movieList = DataLoadState.Error(error = result.error)
                    )
                }
            }
            is Resource.Success -> {
                _state.update {
                    it.copy(
                        movieList = DataLoadState.Loaded(data = result.data)
                    )
                }
            }
        }
    }

    private fun loadPopularMovies() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    movieList = DataLoadState.Loading
                )
            }
            val result = repository.getPopularMovies(page = 10)
            updateMovieListStateBasedOnResult(result)
        }
    }

    private fun loadTopRatedMovies() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    movieList = DataLoadState.Loading
                )
            }
            val result = repository.getTopRatedMovies(page = 10)
            updateMovieListStateBasedOnResult(result)
        }
    }

    private fun loadUpcomingMovies() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    movieList = DataLoadState.Loading
                )
            }
            val result = repository.getUpcomingMovies(page = 10)
            updateMovieListStateBasedOnResult(result)
        }
    }

    fun onEvent(event: MovieListLoadEvent) {
        when(event) {
            MovieListLoadEvent.LoadPopularMovies -> loadPopularMovies()
            MovieListLoadEvent.LoadTopRatedMovies -> loadTopRatedMovies()
            MovieListLoadEvent.LoadUpcomingMovies -> loadUpcomingMovies()
            MovieListLoadEvent.Unknown -> {
                throw Exception("Unknown MovieListLoadEvent")
            }
        }
    }
}