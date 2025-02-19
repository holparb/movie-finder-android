package com.holparb.moviefinder.auth.domain.repository

import com.holparb.moviefinder.core.domain.util.Result
import com.holparb.moviefinder.core.domain.util.errors.LoginError

interface AuthRepository {
    suspend fun login(
        username: String,
        password: String
    ): Result<String, LoginError>
}