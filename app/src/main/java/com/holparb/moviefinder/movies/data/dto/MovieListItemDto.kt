package com.holparb.moviefinder.movies.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieListItemDto(
    val id: Int,
    val title: String,
    val overview: String,
    @SerialName("poster_path")
    val posterPath: String? = null,
    @SerialName("backdrop_path")
    val backdropPath: String? = null,
    @SerialName("release_date")
    val releaseDate: String? = null,
    @SerialName("genre_ids")
    val genreIds: List<Int> = emptyList<Int>(),
    @SerialName("vote_average")
    val rating: Double? = null
)
