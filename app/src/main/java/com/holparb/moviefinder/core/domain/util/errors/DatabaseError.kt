package com.holparb.moviefinder.core.domain.util.errors

import com.holparb.moviefinder.core.domain.util.Error

enum class DatabaseError: Error {
    FETCH_ERROR,
    UPSERT_ERROR,
    DELETE_ERROR
}