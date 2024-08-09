package com.holparb.moviefinder.movies.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.holparb.moviefinder.movies.domain.model.MovieListItem
import com.holparb.moviefinder.movies.domain.repository.MovieRepository
import com.holparb.moviefinder.movies.domain.util.MovieError
import com.holparb.moviefinder.movies.domain.util.Resource
import com.holparb.moviefinder.movies.presentation.events.HomeScreenEvent
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

    private fun updateMovieListState(key: String, newValue: DataLoadState<List<MovieListItem>, MovieError>) {
        val newMap = _state.value.movieLists.toMutableMap()
        newMap[key] = newValue
        _state.update {
            it.copy(
                movieLists = newMap.toMap()
            )
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

    private fun loadPopularMoviesAndMainItem() {
        viewModelScope.launch {
            updateMovieListState(HomeScreenState.popularMovies, DataLoadState.Loading)
            updateMovieListState(HomeScreenState.mainItem, DataLoadState.Loading)
            val result = repository.getPopularMovies()
            updateMovieListStateBasedOnResult(HomeScreenState.popularMovies, result)
            updateMovieListStateBasedOnResult(HomeScreenState.mainItem, result)
        }
    }

    private fun loadTopRatedMovies() {
        viewModelScope.launch {
            updateMovieListState(HomeScreenState.topRatedMovies, DataLoadState.Loading)
            val result = repository.getTopRatedMovies()
            updateMovieListStateBasedOnResult(HomeScreenState.topRatedMovies, result)
        }
    }

    private fun loadUpcomingMovies() {
        viewModelScope.launch {
            updateMovieListState(HomeScreenState.upcomingMovies, DataLoadState.Loading)
            val result = repository.getUpcomingMovies()
            updateMovieListStateBasedOnResult(HomeScreenState.upcomingMovies, result)
        }
    }

    fun onEvent(event: HomeScreenEvent) {
        when(event) {
            HomeScreenEvent.LoadPopularMovies -> loadPopularMoviesAndMainItem()
            HomeScreenEvent.LoadTopRatedMovies -> loadTopRatedMovies()
            HomeScreenEvent.LoadUpcomingMovies -> loadUpcomingMovies()
            HomeScreenEvent.LoadEverything -> {
                loadPopularMoviesAndMainItem()
                loadTopRatedMovies()
                loadUpcomingMovies()
            }
        }
    }
}