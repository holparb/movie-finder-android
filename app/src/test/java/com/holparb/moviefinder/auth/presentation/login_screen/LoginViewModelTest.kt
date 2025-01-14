package com.holparb.moviefinder.auth.presentation.login_screen

import com.holparb.moviefinder.auth.domain.repository.AuthRepository
import com.holparb.moviefinder.core.domain.util.login_form_validator.LoginFormValidator
import io.mockk.clearAllMocks
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
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
}