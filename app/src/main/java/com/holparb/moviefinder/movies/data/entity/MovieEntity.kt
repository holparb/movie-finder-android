package com.holparb.moviefinder.movies.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.holparb.moviefinder.movies.data.entity.type_converter.IntListTypeConverter

@TypeConverters(IntListTypeConverter::class)
@Entity(tableName = "movies")
data class MovieEntity (
    @PrimaryKey val id: Int,
    val title: String,
    val overview: String,
    @ColumnInfo(name = "poster_path") val posterPath: String?,
    @ColumnInfo(name = "backdrop_path") val backdropPath: String?,
    @ColumnInfo(name = "release_date") val releaseDate: String?,
    val runtime: Int? = null,
    val rating: Double? = null,
    @ColumnInfo(name = "genre_ids") val genreIds: List<Int>,
    val details: Boolean = false
)
