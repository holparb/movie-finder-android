package com.holparb.moviefinder.auth.presentation.login_screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.holparb.moviefinder.auth.presentation.login_screen.components.LoginForm

@Composable
fun LoginScreen(modifier: Modifier = Modifier) {
    val loginViewModel = hiltViewModel<LoginViewModel>()
    val state by loginViewModel.state.collectAsStateWithLifecycle()
    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { contentPadding ->
        LoginForm(
            state = state,
            onEvent = loginViewModel::onEvent,
            modifier = Modifier.padding(contentPadding)
        )
    }

}