package com.holparb.moviefinder.movies.presentation.navigation

import android.os.Build
import android.os.Bundle
import androidx.navigation.NavType
import com.holparb.moviefinder.movies.domain.model.MovieListType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object MovieListTypeNavType: NavType<MovieListType> (
    isNullableAllowed = false
) {
    override fun get(bundle: Bundle, key: String): MovieListType? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelable(key, MovieListType::class.java)
        } else {
            @Suppress("DEPRECATION")
            bundle.getParcelable(key)
        }
    }

    override fun parseValue(value: String): MovieListType {
        return Json.decodeFromString<MovieListType>(value)
    }

    override fun put(bundle: Bundle, key: String, value: MovieListType) {
        bundle.putParcelable(key, value)
    }

    override fun serializeAsValue(value: MovieListType): String {
        return Json.encodeToString(value)
    }
}