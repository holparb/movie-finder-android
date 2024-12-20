package com.holparb.moviefinder.core.domain.util

interface LocalEncryptedStorage {

    suspend fun saveSessionId(sessionId: String)

    fun getSessionId(): String?

    suspend fun saveUserId(userId: Int)

    fun getUserId(): Int
}