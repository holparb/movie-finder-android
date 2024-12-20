package com.holparb.moviefinder.auth.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserAccountDto(
    val id: Int,
    val username: String
)
