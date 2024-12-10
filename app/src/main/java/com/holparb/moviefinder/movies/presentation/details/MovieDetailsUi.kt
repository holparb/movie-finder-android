package com.holparb.moviefinder.movies.presentation.details

import android.os.Parcelable
import com.holparb.moviefinder.movies.domain.model.Movie
import kotlinx.parcelize.Parcelize
import java.time.format.DateTimeFormatter

@Parcelize
data class MovieDetailsUi(
    val id: Int = 0,
    val title: String = "",
    val releaseYear: String = "",
    val backdropPath: String? = null,
    val genre: String = "",
    val runtime: String = "",
    val rating: String = "No rating available",
    val overview: String = ""
): Parcelable

fun Movie.toMovieDetailsUi(): MovieDetailsUi {
    return MovieDetailsUi(
        id = id,
        title = title,
        rating = rating?.toString() ?: "No rating available",
        overview = overview,
        backdropPath = backdropPath,
        genre = if(genres.isNotEmpty()) genres.first().name else "",
        runtime = if(runtime != null ) "${runtime / 60}h ${runtime % 60}m" else "",
        releaseYear = releaseDate?.format(DateTimeFormatter.ofPattern("yyyy")) ?: ""
    )
}
