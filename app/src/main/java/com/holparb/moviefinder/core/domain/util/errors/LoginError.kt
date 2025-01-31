package com.holparb.moviefinder.core.domain.util.errors

import com.holparb.moviefinder.core.domain.util.Error

sealed class LoginError: Error {
    data class Network(val networkError: NetworkError): LoginError()
    data class Auth(val authError: AuthError): LoginError()
}