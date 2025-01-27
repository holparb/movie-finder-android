package com.holparb.moviefinder.auth.presentation.login_screen

import app.cash.turbine.test
import com.holparb.moviefinder.auth.domain.repository.AuthRepository
import com.holparb.moviefinder.core.domain.util.Result
import com.holparb.moviefinder.core.domain.util.errors.AuthError
import com.holparb.moviefinder.core.domain.util.errors.LoginError
import com.holparb.moviefinder.core.domain.util.errors.NetworkError
import com.holparb.moviefinder.core.domain.util.login_form_validator.LoginFormValidator
import com.holparb.moviefinder.core.domain.util.login_form_validator.ValidationResult
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class LoginViewModelTest {

    private lateinit var authRepository: AuthRepository
    private lateinit var loginFormValidator: LoginFormValidator
    private lateinit var loginViewModel: LoginViewModel

    @Before
    fun setup() {
        authRepository = mockk()
        loginFormValidator = mockk()
        loginViewModel = LoginViewModel(authRepository = authRepository, loginFormValidator = loginFormValidator)
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun tearDown() {
        clearAllMocks()
        Dispatchers.resetMain()
    }

    @Test
    fun initial_states_are_correct() {
        val loginScreenState = loginViewModel.state.value
        Assert.assertFalse(loginScreenState.loginInProgress)
        Assert.assertTrue(loginScreenState.username.isEmpty())
        Assert.assertTrue(loginScreenState.password.isEmpty())
        Assert.assertNull(loginScreenState.usernameError)
        Assert.assertNull(loginScreenState.passwordError)
    }

    @Test
    fun username_and_password_validation_is_successful() = runTest {
        val username = "username"
        val password = "password"

        every { loginFormValidator.validateUsername(username) } returns ValidationResult(successful = true)
        every { loginFormValidator.validatePassword(password) } returns ValidationResult(successful = true)
        coEvery { authRepository.login(any(), any()) } returns Result.Success("")

        loginViewModel.onEvent(LoginFormEvent.UsernameChanged(username))
        loginViewModel.onEvent(LoginFormEvent.PasswordChanged(password))
        loginViewModel.onEvent(LoginFormEvent.Submit)

        loginViewModel.state.test {
            val updatedState = awaitItem()

            Assert.assertEquals(username, updatedState.username)
            Assert.assertEquals(password, updatedState.password)
            Assert.assertNull(updatedState.usernameError)
            Assert.assertNull(updatedState.passwordError)

            coVerify(exactly = 1) { authRepository.login(any(), any()) }

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun username_validation_fails_and_password_is_successful() = runTest {
        val password = "password"
        val errorMessage = "Error message"

        every { loginFormValidator.validateUsername(any()) } returns ValidationResult(successful = false, errorMessage = errorMessage)
        every { loginFormValidator.validatePassword(password) } returns ValidationResult(successful = true)
        coEvery { authRepository.login(any(), any()) } returns Result.Success("")

        loginViewModel.onEvent(LoginFormEvent.PasswordChanged(password))
        loginViewModel.onEvent(LoginFormEvent.Submit)

        loginViewModel.state.test {
            val updatedState = awaitItem()

            Assert.assertTrue(updatedState.username.isBlank())
            Assert.assertEquals(password, updatedState.password)
            Assert.assertEquals(errorMessage, updatedState.usernameError)
            Assert.assertNull(updatedState.passwordError)

            coVerify(exactly = 0) { authRepository.login(any(), any()) }

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun username_validation_is_successful_and_password_fails() = runTest {
        val username = "username"
        val errorMessage = "Error message"

        every { loginFormValidator.validateUsername(username) } returns ValidationResult(successful = true)
        every { loginFormValidator.validatePassword(any()) } returns ValidationResult(successful = false, errorMessage = errorMessage)
        coEvery { authRepository.login(any(), any()) } returns Result.Success("")

        loginViewModel.onEvent(LoginFormEvent.UsernameChanged(username))
        loginViewModel.onEvent(LoginFormEvent.Submit)

        loginViewModel.state.test {
            val updatedState = awaitItem()

            Assert.assertEquals(username, updatedState.username)
            Assert.assertTrue(updatedState.password.isBlank())
            Assert.assertNull(updatedState.usernameError)
            Assert.assertEquals(errorMessage, updatedState.passwordError)

            coVerify(exactly = 0) { authRepository.login(any(), any()) }

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun username_and_password_validation_fails() = runTest {
        val errorMessage = "Error message"

        every { loginFormValidator.validateUsername(any()) } returns ValidationResult(successful = false, errorMessage = errorMessage)
        every { loginFormValidator.validatePassword(any()) } returns ValidationResult(successful = false, errorMessage = errorMessage)
        coEvery { authRepository.login(any(), any()) } returns Result.Success("")

        loginViewModel.onEvent(LoginFormEvent.Submit)

        loginViewModel.state.test {
            val updatedState = awaitItem()

            Assert.assertEquals(errorMessage, updatedState.usernameError)
            Assert.assertEquals(errorMessage, updatedState.passwordError)

            coVerify(exactly = 0) { authRepository.login(any(), any()) }

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun login_is_successful() = runTest {
        every { loginFormValidator.validateUsername(any()) } returns ValidationResult(successful = true)
        every { loginFormValidator.validatePassword(any()) } returns ValidationResult(successful = true)
        coEvery { authRepository.login(any(), any()) } coAnswers {
            delay(500)
            Result.Success("session_id")
        }

        loginViewModel.onEvent(LoginFormEvent.Submit)
        Assert.assertTrue(loginViewModel.state.value.loginInProgress)

        advanceUntilIdle()

        loginViewModel.state.test {
            Assert.assertFalse(awaitItem().loginInProgress)
            coVerify(exactly = 1) { authRepository.login(any(), any()) }

            cancelAndIgnoreRemainingEvents()
        }
        loginViewModel.channel.test {
            Assert.assertEquals(LoginResult.Success, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun login_fails_with_network_error() = runTest {
        val loginError = LoginError.Network(NetworkError.SERIALIZATION)
        every { loginFormValidator.validateUsername(any()) } returns ValidationResult(successful = true)
        every { loginFormValidator.validatePassword(any()) } returns ValidationResult(successful = true)
        coEvery { authRepository.login(any(), any()) } coAnswers {
            delay(500)
            Result.Error(loginError)
        }

        loginViewModel.onEvent(LoginFormEvent.Submit)
        Assert.assertTrue(loginViewModel.state.value.loginInProgress)

        advanceUntilIdle()

        loginViewModel.state.test {
            Assert.assertFalse(awaitItem().loginInProgress)
            coVerify(exactly = 1) { authRepository.login(any(), any()) }

            cancelAndIgnoreRemainingEvents()
        }

        loginViewModel.channel.test {
            Assert.assertEquals(LoginResult.Failure(loginError), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun login_fails_with_auth_error() = runTest {
        val loginError = LoginError.Auth(AuthError.INVALID_LOGIN_PARAMETERS)
        every { loginFormValidator.validateUsername(any()) } returns ValidationResult(successful = true)
        every { loginFormValidator.validatePassword(any()) } returns ValidationResult(successful = true)
        coEvery { authRepository.login(any(), any()) } coAnswers {
            delay(500)
            Result.Error(loginError)
        }

        loginViewModel.onEvent(LoginFormEvent.Submit)
        Assert.assertTrue(loginViewModel.state.value.loginInProgress)

        advanceUntilIdle()

        loginViewModel.state.test {
            Assert.assertFalse(awaitItem().loginInProgress)
            coVerify(exactly = 1) { authRepository.login(any(), any()) }

            cancelAndIgnoreRemainingEvents()
        }

        loginViewModel.channel.test {
            Assert.assertEquals(LoginResult.Failure(loginError), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}