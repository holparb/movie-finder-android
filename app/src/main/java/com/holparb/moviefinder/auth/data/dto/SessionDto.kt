package com.holparb.moviefinder.auth.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SessionDto(
    val success: Boolean,
    @SerialName("session_id")
    val sessionId: String
)
