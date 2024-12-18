package com.holparb.moviefinder.auth.data.datasource

import com.holparb.moviefinder.auth.data.dto.RequestTokenDto
import com.holparb.moviefinder.auth.data.dto.SessionDto
import com.holparb.moviefinder.auth.data.dto.TokenValidation
import com.holparb.moviefinder.core.data.networking.constructUrl
import com.holparb.moviefinder.core.data.networking.safeCall
import com.holparb.moviefinder.core.domain.util.NetworkError
import com.holparb.moviefinder.core.domain.util.Result
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject

class AuthDataSource @Inject constructor(
    private val httpClient: HttpClient
) {
    suspend fun getRequestToken(): Result<RequestTokenDto, NetworkError> {
        return safeCall<RequestTokenDto> {
            httpClient.get(urlString = constructUrl("/authentication/token/new"))
        }
    }

    suspend fun validateToken(
        username: String,
        password: String,
        requestToken: String
    ): Result<RequestTokenDto, NetworkError> {
        return safeCall<RequestTokenDto> {
            httpClient.post(
                urlString = constructUrl("/authentication/token/validate_with_login")
            ) {
                contentType(ContentType.Application.Json)
                setBody(
                    TokenValidation(
                        username = username,
                        password = password,
                        requestToken = requestToken
                    )
                )
            }
        }
    }

    suspend fun createSession(
        requestToken: RequestTokenDto
    ): Result<SessionDto, NetworkError> {
        return safeCall<SessionDto> {
            httpClient.post(
                urlString = constructUrl("/authentication/session/new")
            ) {
                contentType(ContentType.Application.Json)
                setBody(requestToken)
            }
        }
    }
}