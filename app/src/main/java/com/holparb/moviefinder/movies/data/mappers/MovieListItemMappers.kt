package com.holparb.moviefinder.movies.data.mappers

import com.holparb.moviefinder.movies.data.datasource.remote.TmdbApi
import com.holparb.moviefinder.movies.data.dto.MovieListItemDto
import com.holparb.moviefinder.movies.domain.model.MovieListItem
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun MovieListItemDto.toMovieListItem(): MovieListItem {
    return MovieListItem(
        id = this.id,
        title = this.title,
        releaseDate = if(this.releaseDate.isNullOrBlank()) null else LocalDateTime.parse(this.releaseDate,  DateTimeFormatter.ofPattern("yyyy-mm-dd")),
        posterPath = if(posterPath != null) TmdbApi.IMAGE_URL.plus(this.posterPath) else null
    )
}