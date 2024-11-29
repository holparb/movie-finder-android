package com.holparb.moviefinder.core.domain.util

enum class DatabaseError: Error {
    FETCH_ERROR,
    UPSERT_ERROR,
    DELETE_ERROR
}