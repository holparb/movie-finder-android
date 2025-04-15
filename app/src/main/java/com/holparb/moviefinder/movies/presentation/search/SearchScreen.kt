package com.holparb.moviefinder.movies.presentation.search

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.holparb.moviefinder.movies.presentation.search.components.SearchDisplay

@Composable
fun SearchScreen() {
    val searchViewModel = hiltViewModel<SearchViewModel>()
    val state by searchViewModel.state.collectAsStateWithLifecycle()
    val searchText by searchViewModel.searchText.collectAsStateWithLifecycle()

    SearchDisplay(
        state = state,
        searchText = searchText,
        onAction = searchViewModel::onAction,
        modifier = Modifier.padding(16.dp)
    )
}