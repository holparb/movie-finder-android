package com.holparb.moviefinder.movies.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeyEntity (
    @PrimaryKey
    val id: String,
    val currentPage: Int?,
    val lastUpdated: Long?
) {
    companion object {
        const val POPULAR_MOVIES = "popular_movies"
        const val TOP_RATED_MOVIES = "top_rated_movies"
        const val UPCOMING_MOVIES = "upcoming_movies"
    }
}
