package com.holparb.moviefinder.movies.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.holparb.moviefinder.movies.data.entity.MovieEntity
import com.holparb.moviefinder.movies.data.entity.RemoteKeyEntity

@Dao
interface RemoteKeyDao {
    @Upsert
    suspend fun upsertRemoteKey(remoteKeyEntity: RemoteKeyEntity)

    @Query("DELETE FROM remote_keys")
    suspend fun clearAll()

    @Query("SELECT * FROM remote_keys WHERE id = :id")
    fun getPopularMoviesWithPagination(id: String): PagingSource<Int, MovieEntity>
}