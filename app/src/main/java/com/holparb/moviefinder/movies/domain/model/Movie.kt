package com.holparb.moviefinder.movies.domain.model

import java.time.LocalDate

data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val releaseDate: LocalDate? = null,
    val posterPath: String? = null,
    val backdropPath: String? = null,
    val genres: List<Genre> = emptyList(),
    val rating: Double? = null,
    val popularity: Double = 0.0,
    val runtime: Int? = null
)
