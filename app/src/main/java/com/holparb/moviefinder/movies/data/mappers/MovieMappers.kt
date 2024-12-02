package com.holparb.moviefinder.movies.data.mappers

import android.util.Log
import com.holparb.moviefinder.movies.data.datasource.remote.RemoteMoviesDataSource
import com.holparb.moviefinder.movies.data.dto.MovieDetailsDto
import com.holparb.moviefinder.movies.data.dto.MovieListItemDto
import com.holparb.moviefinder.movies.data.entity.MovieEntity
import com.holparb.moviefinder.movies.domain.model.Genre
import com.holparb.moviefinder.movies.domain.model.Movie
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun MovieListItemDto.toMovie(): Movie {

    return try {
        Movie(
            id = this.id,
            title = this.title,
            overview = this.overview,
            releaseDate = if(this.releaseDate.isNullOrBlank()) null else LocalDate.parse(this.releaseDate,  DateTimeFormatter.ofPattern("yyyy-MM-dd")),
            posterPath = if(!this.posterPath.isNullOrEmpty()) RemoteMoviesDataSource.IMAGE_URL_W500.plus(this.posterPath) else null,
            backdropPath = if(!this.backdropPath.isNullOrEmpty()) RemoteMoviesDataSource.IMAGE_URL_W780.plus(this.backdropPath) else null,
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

fun MovieEntity.toMovie(): Movie {
    return try {
        Movie(
            id = this.id,
            title = this.title,
            overview = this.overview,
            releaseDate = if(this.releaseDate.isNullOrBlank()) null else LocalDate.parse(this.releaseDate,  DateTimeFormatter.ofPattern("yyyy-MM-dd")),
            posterPath = if(!this.posterPath.isNullOrEmpty()) RemoteMoviesDataSource.IMAGE_URL_W500.plus(this.posterPath) else null,
            backdropPath = if(!this.backdropPath.isNullOrEmpty()) RemoteMoviesDataSource.IMAGE_URL_W780.plus(this.backdropPath) else null,
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

fun MovieDetailsDto.toMovie(): Movie {
    return Movie(
        id = id,
        title = title,
        overview = overview,
        releaseDate = if(releaseDate.isNullOrBlank()) null else LocalDate.parse(releaseDate,  DateTimeFormatter.ofPattern("yyyy-MM-dd")),
        posterPath = if(!posterPath.isNullOrEmpty()) RemoteMoviesDataSource.IMAGE_URL_W500.plus(posterPath) else null,
        backdropPath = if(!backdropPath.isNullOrEmpty()) RemoteMoviesDataSource.IMAGE_URL_W780.plus(backdropPath) else null,
        rating = rating,
        runtime = runtime,
        genres = genres.map { genreDto ->
            Genre(id = genreDto.id, name = genreDto.name)
        }
    )
}