package com.holparb.moviefinder.core.data.networking

import com.holparb.moviefinder.core.domain.util.errors.NetworkError
import com.holparb.moviefinder.core.domain.util.Result
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse

suspend inline fun<reified T> responseToResult(
    response: HttpResponse
): Result<T, NetworkError> {
    return when(response.status.value) {
        in 200 .. 299 -> {
            try {
                Result.Success(response.body<T>())
            } catch (e: NoTransformationFoundException) {
                Result.Error(NetworkError.SERIALIZATION)
            }
        }
        422 -> Result.Error(NetworkError.INVALID_REQUEST_PARAMETERS)
        406 -> Result.Error(NetworkError.INVALID_ACCEPT_HEADER)
        in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
        else -> Result.Error(NetworkError.UNKNOWN)
    }
}