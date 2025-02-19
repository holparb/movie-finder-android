package com.holparb.moviefinder.movies.presentation.see_more_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.holparb.moviefinder.movies.domain.model.Movie
import com.holparb.moviefinder.movies.domain.model.MovieListType
import com.holparb.moviefinder.movies.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val repository: MovieRepository
): ViewModel() {

    private var _pagingDataFlow: Flow<PagingData<Movie>> = flowOf(PagingData.empty())
    val pagingDataFlow: Flow<PagingData<Movie>> get() = _pagingDataFlow

    fun loadMovies(listType: MovieListType) {
        viewModelScope.launch {
            _pagingDataFlow = when(listType) {
                MovieListType.PopularMovies -> repository.getPopularMoviesWithPagination().cachedIn(viewModelScope)
                MovieListType.TopRatedMovies -> repository.getTopRatedMoviesWithPagination().cachedIn(viewModelScope)
                MovieListType.UpcomingMovies -> repository.getUpcomingWithPagination().cachedIn(viewModelScope)
            }
        }
    }
}