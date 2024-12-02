package com.holparb.moviefinder.movies.presentation.details

import com.holparb.moviefinder.movies.domain.model.Movie
import java.time.format.DateTimeFormatter

data class MovieDetailsUi(
    val id: Int,
    val title: String,
    val releaseYear: String,
    val backdropPath: String? = null,
    val genre: String,
    val runtime: String,
    val rating: String,
    val overview: String
)

fun Movie.toMovieDetailsUi(): MovieDetailsUi {
    return MovieDetailsUi(
        id = id,
        title = title,
        rating = if(rating != null) rating.toString() else "",
        overview = overview,
        backdropPath = backdropPath,
        genre = if(genres.isNotEmpty()) genres.first().name else "",
        runtime = if(runtime != null ) "${runtime / 60}h ${runtime % 60}m" else "",
        releaseYear = if(releaseDate != null) releaseDate.format(DateTimeFormatter.ofPattern("yyyy")) else ""
    )
}
