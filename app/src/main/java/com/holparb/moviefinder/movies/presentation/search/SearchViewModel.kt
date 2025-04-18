package com.holparb.moviefinder.movies.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.holparb.moviefinder.core.domain.util.pagination.MovieListPaginator
import com.holparb.moviefinder.di.MovieListPaginatorFactory
import com.holparb.moviefinder.movies.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val movieListPaginatorFactory: MovieListPaginatorFactory
): ViewModel() {

    private val movieListPaginator: MovieListPaginator = movieListPaginatorFactory.create(
        onLoadUpdated = { isLoading ->
            _state.update { it.copy(isNewPageLoading = isLoading) }
        },
        onRequest = { page ->
            movieRepository.search(
                query = searchText.value,
                page = page
            )
        },
        onError = { error ->
            println("Search error")
        },
        onSuccess = { movies ->
            if(movies.isEmpty()) {
                _state.update {
                    it.copy(isLastPageReached = true)
                }
            } else {
                _state.update { state ->
                    state.copy(
                        movies = state.movies + movies
                    )
                }
            }
        }
    )

    private val _state = MutableStateFlow(SearchState())
    val state = _state.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.onStart {
        viewModelScope.launch {
            _searchText
                .debounce(500)
                .filter { it.isNotBlank() }
                .distinctUntilChanged()
                .collectLatest { queryText ->
                    _state.update {
                        it.copy(movies = emptyList())
                    }
                    movieListPaginator.reset()
                    movieListPaginator.loadNextPage()
                }
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        ""
    )

    private val _events = Channel<SearchEvent>()
    val events = _events.receiveAsFlow()

    private fun updateSearchText(text: String) {
       _searchText.update { text }
        if(text.isBlank()) {
            _state.update {
                it.copy(movies = emptyList())
            }
        }
    }

    private fun loadMorePages() {
        viewModelScope.launch {
            movieListPaginator.loadNextPage()
        }
    }

    fun onAction(action: SearchAction) {
        when(action) {
            SearchAction.LoadMorePages -> loadMorePages()
            is SearchAction.UpdateSearchText -> updateSearchText(action.text)
        }
    }
}