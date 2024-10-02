package com.holparb.moviefinder.movies.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieListItemDto(
    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "title")
    val title: String,
    @field:Json(name = "overview")
    val overview: String,
    @field:Json(name = "poster_path")
    val posterPath: String? = null,
    @field:Json(name = "backdrop_path")
    val backdropPath: String? = null,
    @field:Json(name = "release_date")
    val releaseDate: String?,
    @field:Json(name = "genre_ids")
    val genreIds: List<Int>,
    @field:Json(name = "vote_average")
    val rating: Double?
)
