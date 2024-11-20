package com.holparb.moviefinder.movies.data.mappers

import android.util.Log
import com.holparb.moviefinder.movies.data.datasource.remote.TmdbApi
import com.holparb.moviefinder.movies.data.dto.MovieListItemDto
import com.holparb.moviefinder.movies.data.entity.MovieEntity
import com.holparb.moviefinder.movies.domain.model.Movie
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun MovieListItemDto.toMovieListItem(): Movie {

    return try {
        Movie(
            id = this.id,
            title = this.title,
            overview = this.overview,
            releaseDate = if(this.releaseDate.isNullOrBlank()) null else LocalDate.parse(this.releaseDate,  DateTimeFormatter.ofPattern("yyyy-MM-dd")),
            posterPath = if(!this.posterPath.isNullOrEmpty()) TmdbApi.IMAGE_URL_W500.plus(this.posterPath) else null,
            backdropPath = if(!this.backdropPath.isNullOrEmpty()) TmdbApi.IMAGE_URL_W780.plus(this.backdropPath) else null,
        )
    }catch (e: Exception) {
        Log.e("MovieListItemDto.toMovieListItem()", e.message ?: "Unknown error")
        Movie(
            id = this.id,
            title = this.title,
            overview = this.overview
        )
    }
}

fun MovieListItemDto.toMovieEntity(isPopular: Boolean = false, isTopRated: Boolean = false, isUpcoming: Boolean = false): MovieEntity {
    return MovieEntity(
        id = this.id,
        title = this.title,
        overview = this.overview,
        releaseDate = this.releaseDate,
        rating = this.rating,
        posterPath = this.posterPath,
        backdropPath = this.backdropPath,
        genreIds = this.genreIds,
        isPopular = isPopular,
        isTopRated = isTopRated,
        isUpcoming = isUpcoming
    )
}

fun MovieEntity.toMovieListItem(): Movie {
    return try {
        Movie(
            id = this.id,
            title = this.title,
            overview = this.overview,
            releaseDate = if(this.releaseDate.isNullOrBlank()) null else LocalDate.parse(this.releaseDate,  DateTimeFormatter.ofPattern("yyyy-MM-dd")),
            posterPath = if(!this.posterPath.isNullOrEmpty()) TmdbApi.IMAGE_URL_W500.plus(this.posterPath) else null,
            backdropPath = if(!this.backdropPath.isNullOrEmpty()) TmdbApi.IMAGE_URL_W780.plus(this.backdropPath) else null,
        )
    }catch (e: Exception) {
        Log.e("MovieListItemDto.toMovieListItem()", e.message ?: "Unknown error")
        Movie(
            id = this.id,
            title = this.title,
            overview = this.overview
        )
    }
}