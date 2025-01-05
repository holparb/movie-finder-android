package com.holparb.moviefinder.auth.presentation.login_screen

import com.holparb.moviefinder.core.domain.util.errors.AuthError

sealed class LoginResult() {
    data object Success: LoginResult()
    data class Failure(val error: AuthError): LoginResult()
}
