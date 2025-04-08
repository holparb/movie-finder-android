package com.holparb.moviefinder.movies.presentation.search

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.holparb.moviefinder.movies.presentation.search.components.SearchDisplay

@Composable
fun SearchScreen() {
    SearchDisplay(Modifier.padding(16.dp))
}