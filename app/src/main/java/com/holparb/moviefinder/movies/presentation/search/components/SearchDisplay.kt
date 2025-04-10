package com.holparb.moviefinder.movies.presentation.search.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.holparb.moviefinder.movies.presentation.search.SearchState

@Composable
fun SearchDisplay(
    state: SearchState,
    searchText: String,
    onSearchTextUpdate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        SearchText(text = searchText, onSearchTextUpdate = onSearchTextUpdate)
        Spacer(Modifier.height(16.dp))
        SearchResultList(movies = state.movies)
        Spacer(Modifier.height(32.dp))
    }
}