package com.holparb.moviefinder.movies.data.mappers

import com.holparb.moviefinder.movies.data.datasource.remote.RemoteMoviesDataSource
import com.holparb.moviefinder.movies.data.dto.MovieDetailsDto
import com.holparb.moviefinder.movies.data.dto.MovieListItemDto
import com.holparb.moviefinder.movies.data.entity.MovieEntity
import com.holparb.moviefinder.movies.domain.model.Movie
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

private fun parseDate(dateString: String?, formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")): LocalDate? {
    return try {
        LocalDate.parse(dateString, formatter)
    }
    catch (_: DateTimeParseException) {
        null
    }
    catch (_: NullPointerException) {
        null
    }
}

fun MovieListItemDto.toMovie(): Movie {
    return Movie(
        id = id,
        title = title,
        overview = overview,
        releaseDate = parseDate(releaseDate),
        posterPath = if(!posterPath.isNullOrEmpty()) RemoteMoviesDataSource.IMAGE_URL_W500.plus(posterPath) else null,
        backdropPath = if(!backdropPath.isNullOrEmpty()) RemoteMoviesDataSource.IMAGE_URL_W780.plus(backdropPath) else null,
        rating = rating,
        popularity = popularity
    )
}

fun MovieListItemDto.toMovieEntity(): MovieEntity {
    return MovieEntity(
        id = id,
        title = title,
        overview = overview,
        releaseDate = releaseDate,
        rating = rating,
        popularity = popularity,
        posterPath = posterPath,
        backdropPath = backdropPath,
        genreIds = genreIds
    )
}

fun MovieEntity.toMovie(): Movie {
    return Movie(
        id = this.id,
        title = this.title,
        overview = this.overview,
        releaseDate = parseDate(releaseDate),
        posterPath = if(!this.posterPath.isNullOrEmpty()) RemoteMoviesDataSource.IMAGE_URL_W500.plus(this.posterPath) else null,
        backdropPath = if(!this.backdropPath.isNullOrEmpty()) RemoteMoviesDataSource.IMAGE_URL_W780.plus(this.backdropPath) else null,
        runtime = runtime,
        rating = rating,
        popularity = popularity,
    )
}

fun MovieDetailsDto.toMovieEntity(
    isPopular: Boolean,
    isTopRated: Boolean,
    isUpcoming: Boolean,
    isWatchlist: Boolean
): MovieEntity {
    return MovieEntity(
        id = id,
        title = title,
        overview = overview,
        releaseDate = releaseDate,
        posterPath = posterPath,
        backdropPath = backdropPath,
        rating = rating,
        popularity = popularity,
        runtime = runtime,
        genreIds = genres.map {
            it.id
        },
        details = true,
        isPopular = isPopular,
        isTopRated = isTopRated,
        isUpcoming = isUpcoming,
        isWatchlist = isWatchlist
    )
}