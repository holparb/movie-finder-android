package com.holparb.moviefinder.movies.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeyEntity (
    @PrimaryKey
    val id: Int,
    val nextPage: Int? = null,
    val lastUpdated: Long? = null
)
