package com.holparb.moviefinder.auth.presentation.login_screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.holparb.moviefinder.R
import com.holparb.moviefinder.auth.presentation.login_screen.LoginFormEvent
import com.holparb.moviefinder.auth.presentation.login_screen.LoginResult
import com.holparb.moviefinder.auth.presentation.login_screen.LoginScreenState
import com.holparb.moviefinder.core.domain.util.errors.LoginError
import com.holparb.moviefinder.core.presentation.util.ObserveAsEvents
import com.holparb.moviefinder.core.presentation.util.toString
import com.holparb.moviefinder.ui.theme.MovieFinderTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun LoginForm(
    state: LoginScreenState,
    events: Flow<LoginResult>,
    onEvent: (LoginFormEvent) -> Unit,
    navigateToWatchlist: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    ObserveAsEvents(events) { loginResult ->
        when(loginResult) {
            is LoginResult.Failure -> {
                val toastText = when(loginResult.error) {
                    is LoginError.Auth -> loginResult.error.authError.toString(context)
                    is LoginError.Network -> loginResult.error.networkError.toString(context)
                }
                onEvent(LoginFormEvent.DisplayToast(toastText))
            }
            LoginResult.Success -> {
                navigateToWatchlist()
            }
        }
    }
    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.tmdb_logo),
            contentDescription = "The Movie Database logo"
        )
        Spacer(Modifier.height(32.dp))
        OutlinedTextField(
            value = state.username,
            label = {
                Text(text = "Username")
            },
            onValueChange = { onEvent(LoginFormEvent.UsernameChanged(it)) },
            isError = state.usernameError != null,
            supportingText = {
                if(state.usernameError != null) {
                    Text(text = state.usernameError, color = MaterialTheme.colorScheme.error)
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
        )
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = state.password,
            label = {
                Text(text = "Password")
            },
            onValueChange = { onEvent(LoginFormEvent.PasswordChanged(it)) },
            isError = state.passwordError != null,
            supportingText = {
                if(state.passwordError != null) {
                    Text(text = state.passwordError, color = MaterialTheme.colorScheme.error)
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            ),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {
                onEvent(LoginFormEvent.Submit)
            },
        ) {
            if(state.loginInProgress) {
                val loadingDescription = stringResource(R.string.loading_description)
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 3.dp,
                    modifier = Modifier
                        .size(24.dp)
                        .semantics { stateDescription = loadingDescription }
                )
            } else {
                Text(text = "Login")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginFormPreview() {
    MovieFinderTheme {
        LoginForm(state = LoginScreenState(), events = emptyFlow(), onEvent = {}, navigateToWatchlist = {})
    }
}
