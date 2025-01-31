package com.holparb.moviefinder.auth.presentation.login_screen.components

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasStateDescription
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.platform.app.InstrumentationRegistry
import com.holparb.moviefinder.R
import com.holparb.moviefinder.auth.presentation.login_screen.LoginFormEvent
import com.holparb.moviefinder.auth.presentation.login_screen.LoginResult
import com.holparb.moviefinder.auth.presentation.login_screen.LoginScreenState
import com.holparb.moviefinder.core.domain.util.errors.AuthError
import com.holparb.moviefinder.core.domain.util.errors.LoginError
import com.holparb.moviefinder.core.presentation.util.toString
import com.holparb.moviefinder.ui.theme.MovieFinderTheme
import io.mockk.clearAllMocks
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginFormTest {

    @get:Rule
    val composeRule = createComposeRule()

    private lateinit var onEvent: (LoginFormEvent) -> Unit
    private lateinit var navigate: () -> Unit

    private lateinit var context: Context

    @Before
    fun setUp() {
        onEvent = mockk<(LoginFormEvent) -> Unit>(relaxed = true)
        navigate = mockk<() -> Unit>(relaxed = true)
        context = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    private fun setContent(
        state: LoginScreenState = LoginScreenState(),
        events: Flow<LoginResult> = emptyFlow<LoginResult>(),
    ) {
        composeRule.setContent {
            MovieFinderTheme {
                LoginForm(
                    state = state,
                    events = events,
                    navigateToWatchlist = navigate,
                    onEvent = onEvent
                )
            }
        }
    }

    @Test
    fun login_failed_event_is_sent() = runTest {
        val _events = Channel<LoginResult>()
        val events = _events.receiveAsFlow()

        setContent(events = events)
        _events.send(LoginResult.Failure(LoginError.Auth(AuthError.INVALID_LOGIN_PARAMETERS)))

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        verify(exactly = 1) { onEvent(LoginFormEvent.DisplayToast(AuthError.INVALID_LOGIN_PARAMETERS.toString(context))) }
    }

    @Test
    fun login_successful_event_is_sent() = runTest {
        val _events = Channel<LoginResult>()
        val events = _events.receiveAsFlow()

        setContent(events = events)
        _events.send(LoginResult.Success)

        verify(exactly = 1) { navigate() }
    }

    @Test
    fun form_display_in_default_state() {
        setContent()

        composeRule.onNode(hasContentDescription("The Movie Database logo")).assertIsDisplayed()
        composeRule.onNodeWithText("Username").assertIsDisplayed()
        composeRule.onNodeWithText("Password").assertIsDisplayed()

        composeRule.onNodeWithText(context.resources.getString(R.string.username_empty_error)).assertIsNotDisplayed()
        composeRule.onNodeWithText(context.resources.getString(R.string.password_empty_error)).assertIsNotDisplayed()
        composeRule.onNodeWithText(context.resources.getString(R.string.password_length_error)).assertIsNotDisplayed()

        composeRule.onNodeWithText("Login").assertIsDisplayed()
        composeRule.onNode(hasStateDescription(context.resources.getString(R.string.loading_description))).assertIsNotDisplayed()
    }

    @Test
    fun username_password_login_button_change_triggers_callback() {
        setContent()
        val text = "some text"
        composeRule.onNodeWithText("Username").performTextInput(text)
        composeRule.onNodeWithText("Password").performTextInput(text)
        composeRule.onNodeWithText("Login").performClick()

        verify { onEvent(LoginFormEvent.UsernameChanged(text)) }
        verify { onEvent(LoginFormEvent.PasswordChanged(text)) }
        verify { onEvent(LoginFormEvent.Submit) }
    }

    @Test
    fun username_and_password_error_display_supporting_text() {
        val usernameError = "username error"
        val passwordError = "password error"
        setContent(state = LoginScreenState(usernameError = usernameError, passwordError = passwordError))

        composeRule.onNodeWithText(usernameError).assertIsDisplayed()
        composeRule.onNodeWithText(passwordError).assertIsDisplayed()
    }

    @Test
    fun login_in_progress() {
        val username = "username"
        val password = "password"
        setContent(state = LoginScreenState(loginInProgress = true, username = username, password = password))

        composeRule.onNodeWithText(username).assertIsDisplayed()
        composeRule.onNodeWithText(password).assertIsDisplayed()
        composeRule.onNodeWithText(context.resources.getString(R.string.username_empty_error)).assertIsNotDisplayed()
        composeRule.onNodeWithText(context.resources.getString(R.string.password_empty_error)).assertIsNotDisplayed()
        composeRule.onNodeWithText(context.resources.getString(R.string.password_length_error)).assertIsNotDisplayed()

        composeRule.onNodeWithText("Login").assertIsNotDisplayed()
        composeRule.onNode(hasStateDescription(context.resources.getString(R.string.loading_description))).assertIsDisplayed()
    }
}