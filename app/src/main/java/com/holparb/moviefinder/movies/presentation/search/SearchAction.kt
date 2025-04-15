package com.holparb.moviefinder.movies.presentation.search

sealed interface SearchAction {
    data class UpdateSearchText(val text: String): SearchAction
    data object LoadMorePages: SearchAction
}