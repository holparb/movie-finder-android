package com.holparb.moviefinder.movies.presentation.states

import com.holparb.moviefinder.movies.domain.model.MovieListItem

data class MainItemState(
    val status: MainItemStatus = MainItemStatus.Loading,
    val mainItem: MovieListItem? = null,
)

enum class MainItemStatus {
    Loading, Error, Loaded
}