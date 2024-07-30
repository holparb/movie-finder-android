package com.holparb.moviefinder.movies.domain.model

import java.time.LocalDateTime

data class MovieListItem(
    val id: Int,
    val title: String,
    val releaseDate: LocalDateTime?,
    val posterPath: String?
)
