package com.holparb.moviefinder.auth.domain.repository

import com.holparb.moviefinder.core.domain.util.Error
import com.holparb.moviefinder.core.domain.util.Result

interface AuthRepository {
    suspend fun login(
        username: String,
        password: String
    ): Result<String, Error>
}