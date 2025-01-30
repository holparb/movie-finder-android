package com.holparb.moviefinder.auth.presentation.login_screen.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
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

    @Before
    fun setUp() {
        onEvent = mockk<(LoginFormEvent) -> Unit>(relaxed = true)
        navigate = mockk<() -> Unit>(relaxed = true)
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
        var _events = Channel<LoginResult>()
        val events = _events.receiveAsFlow()

        setContent(events = events)
        _events.send(LoginResult.Failure(LoginError.Auth(AuthError.INVALID_LOGIN_PARAMETERS)))

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        verify(exactly = 1) { onEvent(LoginFormEvent.DisplayToast(AuthError.INVALID_LOGIN_PARAMETERS.toString(context))) }
    }

    @Test
    fun login_successful_event_is_sent() = runTest {
        var _events = Channel<LoginResult>()
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

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        composeRule.onNodeWithText(context.resources.getString(R.string.username_empty_error)).assertIsNotDisplayed()
        composeRule.onNodeWithText(context.resources.getString(R.string.password_empty_error)).assertIsNotDisplayed()
        composeRule.onNodeWithText(context.resources.getString(R.string.password_length_error)).assertIsNotDisplayed()

        composeRule.onNodeWithText("Login").assertIsDisplayed()
    }

    @Test
    fun username_and_password_change_triggers_callback() {
        setContent()
        val text = "some text"
        composeRule.onNodeWithText("Username").performTextInput(text)
        composeRule.onNodeWithText("Password").performTextInput(text)

        verify { onEvent(LoginFormEvent.UsernameChanged(text)) }
        verify { onEvent(LoginFormEvent.PasswordChanged(text)) }
    }
}