package com.holparb.moviefinder.movies.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDetailsDto(
    val id: Int,
    val title: String,
    val overview: String,
    @SerialName("poster_path")
    val posterPath: String? = null,
    @SerialName("backdrop_path")
    val backdropPath: String? = null,
    @SerialName("release_date")
    val releaseDate: String? = null,
    @SerialName("vote_average")
    val rating: Double? = null,
    val popularity: Double? = null,
    val runtime: Int? = null,
    val genres: List<GenreDto> = emptyList<GenreDto>()
)
