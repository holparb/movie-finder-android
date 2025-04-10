package com.holparb.moviefinder.movies.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.holparb.moviefinder.core.domain.util.onError
import com.holparb.moviefinder.core.domain.util.onSuccess
import com.holparb.moviefinder.movies.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val movieRepository: MovieRepository
): ViewModel() {

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
                    performSearch(queryText)
                }
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        ""
    )

    fun updateSearchText(text: String) {
       _searchText.update { text }
    }

    private suspend fun performSearch(queryText: String) {
        _state.update { it.copy(isLoading = true) }
        movieRepository.search(queryText)
            .onError { error ->
                _state.update { it.copy(isLoading = false) }
            }
            .onSuccess { movies ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        movies = movies
                    )
                }
            }
    }
}