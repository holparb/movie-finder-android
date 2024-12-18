package com.holparb.moviefinder.auth.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TokenValidation(
    val username: String,
    val password: String,
    @SerialName("request_token")
    val requestToken: String
)
