package com.holparb.moviefinder.movies.presentation.home_screen

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.holparb.moviefinder.core.domain.util.NetworkError
import com.holparb.moviefinder.core.domain.util.Result
import com.holparb.moviefinder.movies.domain.model.Movie
import com.holparb.moviefinder.movies.domain.repository.MovieRepository
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
        result: Result<List<Movie>, NetworkError>
    ) {
        when(result) {
            is Result.Error -> {
                updateMovieListState(key, isLoading = false)
            }
            is Result.Success -> {
                updateMovieListState(key, movies = result.data, isLoading = false)
            }
        }
    }




    fun loadPopularMoviesAndMainItem() {
        viewModelScope.launch {
            updateMovieListState(HomeScreenState.POPULAR_MOVIES, isLoading = true)
            updateMovieListState(HomeScreenState.MAIN_ITEM, isLoading = true)
            val result = repository.getPopularMovies()
            updateMovieListStateBasedOnResult(HomeScreenState.POPULAR_MOVIES, result)
            updateMovieListStateBasedOnResult(HomeScreenState.MAIN_ITEM, result)
        }
    }

    fun loadTopRatedMovies() {
        viewModelScope.launch {
            updateMovieListState(HomeScreenState.TOP_RATED_MOVIES, isLoading = true)
            val result = repository.getTopRatedMovies()
            updateMovieListStateBasedOnResult(HomeScreenState.TOP_RATED_MOVIES, result)
        }
    }

    fun loadUpcomingMovies() {
        viewModelScope.launch {
            updateMovieListState(HomeScreenState.UPCOMING_MOVIES, isLoading = true)
            val result = repository.getUpcomingMovies()
            updateMovieListStateBasedOnResult(HomeScreenState.UPCOMING_MOVIES, result)
        }
    }
}