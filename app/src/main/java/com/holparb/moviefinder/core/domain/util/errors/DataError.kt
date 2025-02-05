package com.holparb.moviefinder.core.domain.util.errors

import com.holparb.moviefinder.core.domain.util.Error

sealed class DataError: Error {
    data class Network(val networkError: NetworkError): DataError()
    data class Database(val databaseError: DatabaseError): DataError()
}