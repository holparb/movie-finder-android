package com.holparb.moviefinder.movies.presentation.see_more_screen.components

import com.holparb.moviefinder.core.presentation.util.DisplayableNumber
import com.holparb.moviefinder.core.presentation.util.toDisplayableNumber
import com.holparb.moviefinder.movies.domain.model.Movie
import java.time.format.DateTimeFormatter

data class MovieVerticalListItemUi(
    val id: Int = 0,
    val title: String = "",
    val releaseYear: String = "",
    val posterPath: String? = null,
    val rating: DisplayableNumber? = null,
    val overview: String = ""
)

fun Movie.toMovieVerticalListItemUi(): MovieVerticalListItemUi {
    return MovieVerticalListItemUi(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath,
        rating = rating?.toDisplayableNumber(),
        releaseYear = releaseDate?.format(DateTimeFormatter.ofPattern("yyyy")) ?: ""
    )
}
