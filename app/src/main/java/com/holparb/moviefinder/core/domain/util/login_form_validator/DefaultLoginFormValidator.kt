package com.holparb.moviefinder.core.domain.util.login_form_validator

import android.content.Context
import com.holparb.moviefinder.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DefaultLoginFormValidator @Inject constructor(
    @ApplicationContext private val context: Context
): LoginFormValidator {
    override fun validateUsername(username: String): ValidationResult {
        if(username.isBlank()) return ValidationResult(successful = false, errorMessage = context.getString(
            R.string.username_empty_error))

        return ValidationResult(successful = true)
    }

    override fun validatePassword(password: String): ValidationResult {
        if(password.isBlank()) return ValidationResult(successful = false, errorMessage = context.getString(R.string.password_empty_error))

        if(password.length < 4) return ValidationResult(successful = false, errorMessage = context.getString(R.string.password_length_error))

        return ValidationResult(successful = true)
    }
}