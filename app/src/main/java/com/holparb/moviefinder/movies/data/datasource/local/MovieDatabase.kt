package com.holparb.moviefinder.movies.data.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.holparb.moviefinder.movies.data.dao.MovieDao
import com.holparb.moviefinder.movies.data.entity.GenreEntity
import com.holparb.moviefinder.movies.data.entity.MovieEntity

@Database(
    entities = [MovieEntity::class, GenreEntity::class],
    version = 1
)
abstract class MovieDatabase: RoomDatabase() {

    abstract val movieDao: MovieDao

}