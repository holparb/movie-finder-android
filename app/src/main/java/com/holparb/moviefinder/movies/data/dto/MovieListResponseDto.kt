package com.holparb.moviefinder.movies.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class MovieListResponseDto(
    val page: Int,
    val results: List<MovieListItemDto>
)
