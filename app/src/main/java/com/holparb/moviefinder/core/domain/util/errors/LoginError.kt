package com.holparb.moviefinder.core.domain.util.errors

import com.holparb.moviefinder.core.domain.util.Error

sealed class LoginError: Error {
    data class Network(val error: NetworkError): LoginError()
    data class Auth(val error: AuthError): LoginError()
}