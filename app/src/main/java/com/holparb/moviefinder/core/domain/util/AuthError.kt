package com.holparb.moviefinder.core.domain.util

enum class AuthError : Error {
    TOKEN_REQUEST_ERROR,
    INVALID_LOGIN_PARAMETERS,
    SESSION_CREATION_ERROR
}