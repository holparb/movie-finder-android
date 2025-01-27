package com.holparb.moviefinder.auth.presentation.login_screen

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.holparb.moviefinder.auth.presentation.login_screen.components.LoginForm
import com.holparb.moviefinder.core.domain.util.errors.LoginError
import com.holparb.moviefinder.core.presentation.util.ObserveAsEvents
import com.holparb.moviefinder.core.presentation.util.toString

@Composable
fun LoginScreen(
    navigateToWatchlist: () -> Unit
) {
    val loginViewModel = hiltViewModel<LoginViewModel>()
    val state by loginViewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    ObserveAsEvents(loginViewModel.channel) { loginResult ->
        when(loginResult) {
            is LoginResult.Failure -> {
                val toastText = when(loginResult.error) {
                    is LoginError.Auth -> loginResult.error.authError.toString(context)
                    is LoginError.Network -> loginResult.error.networkError.toString(context)
                }
                Toast.makeText(
                    context,
                    toastText,
                    Toast.LENGTH_LONG
                ).show()
            }
            LoginResult.Success -> {
                navigateToWatchlist()
            }
        }
    }
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { contentPadding ->
        LoginForm(
            state = state,
            onEvent = loginViewModel::onEvent,
            modifier = Modifier.padding(contentPadding)
        )
    }

}