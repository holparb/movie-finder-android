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
fun LoginScreen(
    navigateToWatchlist: () -> Unit
) {
    val loginViewModel = hiltViewModel<LoginViewModel>()
    val state by loginViewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { contentPadding ->
        LoginForm(
            state = state,
            events = loginViewModel.channel,
            onEvent = loginViewModel::onEvent,
            navigateToWatchlist = navigateToWatchlist,
            modifier = Modifier.padding(contentPadding)
        )
    }

}