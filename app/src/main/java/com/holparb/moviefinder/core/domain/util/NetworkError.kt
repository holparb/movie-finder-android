package com.holparb.moviefinder.core.domain.util

enum class NetworkError: Error{
    AUTH_FAILED,
    NO_INTERNET_CONNECTION,
    SERVER_ERROR,
    INVALID_ACCEPT_HEADER,
    INVALID_REQUEST_PARAMETERS,
    SERIALIZATION,
    UNKNOWN
}