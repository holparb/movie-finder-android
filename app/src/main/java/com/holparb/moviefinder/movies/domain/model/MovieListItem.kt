package com.holparb.moviefinder.movies.domain.model

import java.time.LocalDate

data class MovieListItem(
    val id: Int,
    val title: String,
    val overview: String,
    val releaseDate: LocalDate?,
    val posterPath: String?,
    val backdropPath: String?
)
