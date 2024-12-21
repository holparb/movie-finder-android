package com.holparb.moviefinder.auth.data.repository

import com.holparb.moviefinder.auth.data.datasource.AuthDataSource
import com.holparb.moviefinder.auth.domain.repository.AuthRepository
import com.holparb.moviefinder.core.domain.util.AuthError
import com.holparb.moviefinder.core.domain.util.Error
import com.holparb.moviefinder.core.domain.util.LocalEncryptedStorage
import com.holparb.moviefinder.core.domain.util.Result
import com.holparb.moviefinder.core.domain.util.onError
import com.holparb.moviefinder.core.domain.util.onSuccess
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authDataSource: AuthDataSource,
    private val localEncryptedStorage: LocalEncryptedStorage
) : AuthRepository {
    override suspend fun login(
        username: String,
        password: String
    ): Result<String, Error> {
        var sessionId: String? = null
        // Get request token
        authDataSource.getRequestToken()
            .onError {
                return Result.Error(it)
            }
            .onSuccess { requestTokenResponse ->
                if(requestTokenResponse.success.not()) {
                    return Result.Error(AuthError.TOKEN_REQUEST_ERROR)
                }
                // Validate token using username/password
                authDataSource.validateToken(username, password, requestTokenResponse.requestToken)
                    .onError {
                        return Result.Error(it)
                    }
                    .onSuccess { tokenValidationResponse ->
                        if(tokenValidationResponse.success.not()) {
                            return Result.Error(AuthError.INVALID_LOGIN_PARAMETERS)
                        }
                        // Get sessionId using validated token
                        authDataSource.createSession(tokenValidationResponse)
                            .onError {
                                return Result.Error(it)
                            }
                            .onSuccess { sessionResponse ->
                                if(sessionResponse.success) {
                                    sessionId = sessionResponse.sessionId
                                }
                            }
                    }
            }
        if(sessionId == null) {
            return Result.Error(AuthError.SESSION_CREATION_ERROR)
        }
        localEncryptedStorage.saveSessionId(sessionId!!)
        return Result.Success(sessionId!!)
    }

    override fun isUserLoggedIn(): Boolean {
        return localEncryptedStorage.getSessionId() != null
    }
}