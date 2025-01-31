package com.holparb.moviefinder.core.domain.util.login_form_validator

data class ValidationResult(
    val successful: Boolean = true,
    val errorMessage: String? = null
)
