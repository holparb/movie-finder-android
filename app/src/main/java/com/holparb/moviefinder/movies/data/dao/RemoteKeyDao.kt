package com.holparb.moviefinder.movies.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.holparb.moviefinder.movies.data.entity.RemoteKeyEntity

@Dao
interface RemoteKeyDao {
    @Upsert
    suspend fun upsertRemoteKey(remoteKeyEntity: RemoteKeyEntity)

    @Query("DELETE FROM remote_keys")
    suspend fun clearAll()

    @Query("DELETE FROM remote_keys WHERE id ")
    suspend fun deleteRemoteKeyById(id: Int)

    @Query("SELECT * FROM remote_keys WHERE id = :id")
    suspend fun getRemoteKeyById(id: Int): RemoteKeyEntity?
}