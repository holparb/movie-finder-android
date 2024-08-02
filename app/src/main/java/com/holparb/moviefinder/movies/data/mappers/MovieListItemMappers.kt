package com.holparb.moviefinder.movies.data.mappers

import com.holparb.moviefinder.movies.data.datasource.remote.TmdbApi
import com.holparb.moviefinder.movies.data.dto.MovieListItemDto
import com.holparb.moviefinder.movies.domain.model.MovieListItem
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun MovieListItemDto.toMovieListItem(): MovieListItem {
    return MovieListItem(
        id = this.id,
        title = this.title,
        releaseDate = if(this.releaseDate.isNullOrBlank()) null else LocalDate.parse(this.releaseDate,  DateTimeFormatter.ofPattern("yyyy-MM-dd")),
        posterPath = if(!this.posterPath.isNullOrEmpty()) TmdbApi.IMAGE_URL_W500.plus(this.posterPath) else null,
        backdropPath = if(!this.backdropPath.isNullOrEmpty()) TmdbApi.IMAGE_URL_W780.plus(this.backdropPath) else null,
    )
}