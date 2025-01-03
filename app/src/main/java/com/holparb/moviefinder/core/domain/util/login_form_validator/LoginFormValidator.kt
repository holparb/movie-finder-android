package com.holparb.moviefinder.core.domain.util.login_form_validator

interface LoginFormValidator {

    fun validateUsername(username: String): ValidationResult

    fun validatePassword(password: String): ValidationResult
}