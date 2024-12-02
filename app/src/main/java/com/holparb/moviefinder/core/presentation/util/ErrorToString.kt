package com.holparb.moviefinder.core.presentation.util

import android.content.Context
import com.holparb.moviefinder.R
import com.holparb.moviefinder.core.domain.util.DatabaseError
import com.holparb.moviefinder.core.domain.util.NetworkError

fun NetworkError.toString(context: Context): String {
    val resId = when(this) {
        NetworkError.NO_INTERNET_CONNECTION -> R.string.error_no_internet
        NetworkError.SERVER_ERROR -> R.string.error_unknown
        NetworkError.SERIALIZATION -> R.string.error_serialization
        NetworkError.UNKNOWN -> R.string.error_unknown
        NetworkError.AUTH_FAILED -> R.string.auth_failed
        NetworkError.INVALID_ACCEPT_HEADER -> R.string.invalid_request
        NetworkError.INVALID_REQUEST_PARAMETERS -> R.string.invalid_request
    }
    return context.getString(resId)
}

fun DatabaseError.toString(context: Context): String {
    val resId = when(this) {
        DatabaseError.FETCH_ERROR -> R.string.db_fetch_error
        DatabaseError.DELETE_ERROR -> R.string.db_delete_error
        DatabaseError.UPSERT_ERROR -> R.string.db_upsert_error
    }
    return context.getString(resId)
}