package com.holparb.moviefinder.movies.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.holparb.moviefinder.movies.domain.model.MovieListItem
import com.holparb.moviefinder.movies.domain.repository.MovieRepository
import com.holparb.moviefinder.movies.presentation.events.MovieListLoadEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val repository: MovieRepository,
): ViewModel() {

    private var _pagingDataFlow: Flow<PagingData<MovieListItem>> = flowOf(PagingData.empty())
    val pagingDataFlow: Flow<PagingData<MovieListItem>> get() = _pagingDataFlow

    fun loadMovies(event: MovieListLoadEvent) {
        viewModelScope.launch {
            _pagingDataFlow = when(event) {
                MovieListLoadEvent.LoadPopularMovies -> repository.getPopularMoviesWithPagination().cachedIn(viewModelScope)
                MovieListLoadEvent.LoadTopRatedMovies -> repository.getTopRatedMoviesWithPagination().cachedIn(viewModelScope)
                MovieListLoadEvent.LoadUpcomingMovies -> repository.getUpcomingWithPagination().cachedIn(viewModelScope)
                MovieListLoadEvent.Unknown -> {
                    throw Exception("Unknown MovieListLoadEvent")
                }
            }
        }
    }
}