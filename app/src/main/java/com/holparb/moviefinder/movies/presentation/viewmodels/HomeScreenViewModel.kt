package com.holparb.moviefinder.movies.presentation.viewmodels

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.holparb.moviefinder.movies.domain.model.MovieListItem
import com.holparb.moviefinder.movies.domain.repository.MovieRepository
import com.holparb.moviefinder.movies.domain.util.MovieError
import com.holparb.moviefinder.core.utils.Resource
import com.holparb.moviefinder.movies.presentation.states.DataLoadState
import com.holparb.moviefinder.movies.presentation.states.HomeScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val repository: MovieRepository
): ViewModel() {

    private val _state = MutableStateFlow(HomeScreenState())
    val state = _state.asStateFlow()

    @VisibleForTesting
    private fun updateMovieListState(key: String, newValue: DataLoadState<List<MovieListItem>, MovieError>) {
        val newMap = _state.value.movieLists.toMutableMap()
        newMap[key]?.let {
            newMap[key] = it.copy(movieList = newValue)
            _state.update { homeScreenState ->
                homeScreenState.copy(
                    movieLists = newMap.toMap()
                )
            }
        }
    }

    private fun updateMovieListStateBasedOnResult(
        key: String,
        result: Resource<List<MovieListItem>, MovieError>
    ) {
        when(result) {
            is Resource.Error -> {
                updateMovieListState(key, DataLoadState.Error(error = result.error))
            }
            is Resource.Success -> {
                updateMovieListState(key, DataLoadState.Loaded(data = result.data))
            }
        }
    }

    fun loadPopularMoviesAndMainItem() {
        viewModelScope.launch {
            updateMovieListState(HomeScreenState.POPULAR_MOVIES, DataLoadState.Loading)
            updateMovieListState(HomeScreenState.MAIN_ITEM, DataLoadState.Loading)
            val result = repository.getPopularMovies()
            updateMovieListStateBasedOnResult(HomeScreenState.POPULAR_MOVIES, result)
            updateMovieListStateBasedOnResult(HomeScreenState.MAIN_ITEM, result)
        }
    }

    fun loadTopRatedMovies() {
        viewModelScope.launch {
            updateMovieListState(HomeScreenState.TOP_RATED_MOVIES, DataLoadState.Loading)
            val result = repository.getTopRatedMovies()
            updateMovieListStateBasedOnResult(HomeScreenState.TOP_RATED_MOVIES, result)
        }
    }

    fun loadUpcomingMovies() {
        viewModelScope.launch {
            updateMovieListState(HomeScreenState.UPCOMING_MOVIES, DataLoadState.Loading)
            val result = repository.getUpcomingMovies()
            updateMovieListStateBasedOnResult(HomeScreenState.UPCOMING_MOVIES, result)
        }
    }
}