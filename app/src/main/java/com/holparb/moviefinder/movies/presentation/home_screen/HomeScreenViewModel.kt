package com.holparb.moviefinder.movies.presentation.home_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.holparb.moviefinder.core.domain.util.Result
import com.holparb.moviefinder.core.domain.util.errors.DataError
import com.holparb.moviefinder.core.domain.util.onError
import com.holparb.moviefinder.core.domain.util.onSuccess
import com.holparb.moviefinder.movies.domain.model.Movie
import com.holparb.moviefinder.movies.domain.repository.MovieRepository
import com.holparb.moviefinder.movies.domain.util.MovieEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val repository: MovieRepository
): ViewModel() {

    private val _state = MutableStateFlow(HomeScreenState())
    val state = _state.asStateFlow()

    init {
        loadHomeScreenContent()
    }

    private val _events = Channel<MovieEvent>()
    val events = _events.receiveAsFlow()

    private var _error: DataError? = null

    private fun updateMovieListState(key: String, movies: List<Movie> = emptyList(), isLoading: Boolean) {
        val newMap = _state.value.movieLists.toMutableMap()
        newMap[key]?.let {
            if(isLoading) {
                newMap[key] = it.copy(isLoading = true)
            } else {
                newMap[key] = it.copy(isLoading = false, movieList = movies)
            }
            _state.update { homeScreenState ->
                homeScreenState.copy(
                    movieLists = newMap.toMap()
                )
            }
        }
    }

    private fun updateMovieListStateBasedOnResult(
        key: String,
        result: Result<List<Movie>, DataError>
    ) {
        _error = null
        result
            .onSuccess { movies ->
                updateMovieListState(key, movies = movies, isLoading = false)
            }
            .onError { error ->
                updateMovieListState(key, isLoading = false)
                _error = error
            }
    }

    private fun loadHomeScreenContent() {
        loadPopularMoviesAndMainItem()
        loadTopRatedMovies()
        loadUpcomingMovies()
        viewModelScope.launch {
            if(_error != null) {
                when(_error) {
                    is DataError.Network -> _events.send(MovieEvent.RemoteError((_error as DataError.Network).networkError))
                    is DataError.Database -> _events.send(MovieEvent.LocalError((_error as DataError.Database).databaseError))
                    else -> { /* _error can not be null */ }
                }
            }
        }
    }

    private fun loadPopularMoviesAndMainItem() {
        updateMovieListState(HomeScreenState.POPULAR_MOVIES, isLoading = true)
        updateMovieListState(HomeScreenState.MAIN_ITEM, isLoading = true)
        viewModelScope.launch {
            val result = repository.getPopularMovies()
            updateMovieListStateBasedOnResult(HomeScreenState.POPULAR_MOVIES, result)
            updateMovieListStateBasedOnResult(HomeScreenState.MAIN_ITEM, result)
        }
    }

    private fun loadTopRatedMovies() {
        updateMovieListState(HomeScreenState.TOP_RATED_MOVIES, isLoading = true)
        viewModelScope.launch {
            val result = repository.getTopRatedMovies()
            updateMovieListStateBasedOnResult(HomeScreenState.TOP_RATED_MOVIES, result)
        }
    }

    private fun loadUpcomingMovies() {
        updateMovieListState(HomeScreenState.UPCOMING_MOVIES, isLoading = true)
        viewModelScope.launch {
            val result = repository.getUpcomingMovies()
            updateMovieListStateBasedOnResult(HomeScreenState.UPCOMING_MOVIES, result)
        }
    }
}