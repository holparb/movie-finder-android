package com.holparb.moviefinder.auth.presentation.login_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.holparb.moviefinder.auth.domain.repository.AuthRepository
import com.holparb.moviefinder.core.domain.util.login_form_validator.LoginFormValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginFormValidator: LoginFormValidator,
    private val authRepository: AuthRepository
): ViewModel() {

    private val _state = MutableStateFlow(LoginScreenState())
    val state = _state.asStateFlow()

    private val _channel = Channel<LoginResult>()
    val channel = _channel.receiveAsFlow()

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

    private fun isFormValid(): Boolean {
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
            return false
        }
        _state.update {
            it.copy(
                usernameError = null,
                passwordError = null
            )
        }
        return true
    }

    private fun submitData() {
        if(isFormValid().not()) return
        viewModelScope.launch {
            
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