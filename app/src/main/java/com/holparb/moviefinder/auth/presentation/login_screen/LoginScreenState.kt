package com.holparb.moviefinder.auth.presentation.login_screen

data class LoginScreenState (
    val username: String = "",
    val password: String = "",
    val usernameError: String? = null,
    val passwordError: String? = null
)