package com.holparb.moviefinder.movies.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieListResponseDto(
    @field:Json(name = "page")
    val page: Int,
    @field:Json(name = "results")
    val results: List<MovieListItemDto>
)
