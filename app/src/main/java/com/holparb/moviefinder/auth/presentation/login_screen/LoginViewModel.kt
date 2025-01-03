package com.holparb.moviefinder.auth.presentation.login_screen

import androidx.lifecycle.ViewModel
import com.holparb.moviefinder.auth.domain.repository.AuthRepository
import com.holparb.moviefinder.core.domain.util.login_form_validator.LoginFormValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginFormValidator: LoginFormValidator,
    private val authRepository: AuthRepository
): ViewModel() {

    private val _state = MutableStateFlow(LoginScreenState())
    val state = _state.asStateFlow()

    private fun updateUsername(text: String) {
        _state.update {
            it.copy(
                username = text
            )
        }
    }

    private fun updatePassword(text: String) {
        _state.update {
            it.copy(
                password = text
            )
        }
    }

    private fun submitData() {
        val userNameValidationResult = loginFormValidator.validateUsername(state.value.username)
        val passwordValidationResult = loginFormValidator.validatePassword(state.value.password)
        val hasError = listOf(
            userNameValidationResult,
            passwordValidationResult
        ).any {
            it.successful.not()
        }

        if(hasError) {
            _state.update {
                it.copy(
                    usernameError = userNameValidationResult.errorMessage,
                    passwordError = passwordValidationResult.errorMessage
                )
            }
            return
        } else {
            _state.update {
                it.copy(
                    usernameError = null,
                    passwordError = null
                )
            }
        }
    }

    fun onEvent(event: LoginFormEvent) {
        when(event) {
            LoginFormEvent.Submit -> submitData()
            is LoginFormEvent.PasswordChanged -> updatePassword(event.password)
            is LoginFormEvent.UsernameChanged -> updateUsername(event.username)
        }
    }
}