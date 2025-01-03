package com.holparb.moviefinder.auth.presentation.login_screen

sealed class LoginFormEvent {
    data class UsernameChanged(val username: String): LoginFormEvent()
    data class PasswordChanged(val password: String): LoginFormEvent()
    data object Submit: LoginFormEvent()
}