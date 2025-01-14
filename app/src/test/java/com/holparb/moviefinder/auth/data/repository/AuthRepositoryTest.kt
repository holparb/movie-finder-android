package com.holparb.moviefinder.auth.data.repository

import com.holparb.moviefinder.auth.data.datasource.AuthDataSource
import com.holparb.moviefinder.auth.data.dto.RequestTokenDto
import com.holparb.moviefinder.auth.data.dto.SessionDto
import com.holparb.moviefinder.auth.domain.repository.AuthRepository
import com.holparb.moviefinder.core.domain.util.LocalEncryptedStorage
import com.holparb.moviefinder.core.domain.util.Result
import com.holparb.moviefinder.core.domain.util.errors.AuthError
import com.holparb.moviefinder.core.domain.util.errors.NetworkError
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class AuthRepositoryTest {

    private lateinit var authDataSource: AuthDataSource
    private lateinit var localEncryptedStorage: LocalEncryptedStorage
    private lateinit var authRepository: AuthRepository

    @Before
    fun setup() {
        authDataSource = mockk()
        localEncryptedStorage = mockk()
        authRepository = AuthRepositoryImpl(authDataSource, localEncryptedStorage)
    }

    @Test
    fun stored_session_id_is_not_null() {
        every { localEncryptedStorage.getSessionId() } returns "123456789"
        Assert.assertTrue(authRepository.isUserLoggedIn())
    }

    @Test
    fun stored_session_id_is_null() {
        every { localEncryptedStorage.getSessionId() } returns null
        Assert.assertFalse(authRepository.isUserLoggedIn())
    }

    @Test
    fun login_is_successful() = runBlocking {
        val requestTokenDto = RequestTokenDto(
            success = true,
            requestToken = "abcd123efg",
            expiresAt = "2025-06-17"
        )
        val sessionDto = SessionDto(
            success = true,
            sessionId = "32u4389hdfns4584"
        )
        coEvery { authDataSource.getRequestToken() } returns Result.Success(requestTokenDto)
        coEvery { authDataSource.validateToken(any(), any(), any()) } returns Result.Success(requestTokenDto)
        coEvery { authDataSource.createSession(requestTokenDto) } returns Result.Success(sessionDto)
        coEvery { localEncryptedStorage.saveSessionId(any()) } returns Unit

        val result = authRepository.login("username", "password")
        Assert.assertTrue(result is Result.Success)
        Assert.assertTrue((result as Result.Success).data.isNotBlank())
        Assert.assertEquals(sessionDto.sessionId, result.data)
    }

    @Test
    fun login_fails_at_token_request() = runBlocking {
        val networkError = NetworkError.NO_INTERNET_CONNECTION
        coEvery { authDataSource.getRequestToken() } returns Result.Error(networkError)
        val result = authRepository.login("username", "password")
        Assert.assertTrue(result is Result.Error)
        Assert.assertEquals(networkError, (result as Result.Error).error)
    }

    @Test
    fun login_fails_because_of_unsuccessful_token_request() = runBlocking {
        coEvery { authDataSource.getRequestToken() } returns Result.Success(RequestTokenDto(success = false, "", ""))
        val result = authRepository.login("username", "password")
        Assert.assertTrue(result is Result.Error)
        Assert.assertEquals(AuthError.TOKEN_REQUEST_ERROR, (result as Result.Error).error)
    }

    @Test
    fun login_fails_at_token_validation() = runBlocking {
        val networkError = NetworkError.SERIALIZATION
        coEvery { authDataSource.getRequestToken() } returns Result.Success(RequestTokenDto(success = true, "", ""))
        coEvery { authDataSource.validateToken(any(), any(), any()) } returns Result.Error(networkError)
        val result = authRepository.login("username", "password")
        Assert.assertTrue(result is Result.Error)
        Assert.assertEquals(networkError, (result as Result.Error).error)
    }

    @Test
    fun login_fails_because_of_unsuccessful_token_validation() = runBlocking {
        coEvery { authDataSource.getRequestToken() } returns Result.Success(RequestTokenDto(success = true, "", ""))
        coEvery { authDataSource.validateToken(any(), any(), any()) } returns Result.Success(RequestTokenDto(success = false, "", ""))
        val result = authRepository.login("username", "password")
        Assert.assertTrue(result is Result.Error)
        Assert.assertEquals(AuthError.INVALID_LOGIN_PARAMETERS, (result as Result.Error).error)
    }

    @Test
    fun login_fails_at_session_creation() = runBlocking {
        val networkError = NetworkError.SERVER_ERROR
        val requestTokenDto = RequestTokenDto(success = true, "", "")
        coEvery { authDataSource.getRequestToken() } returns Result.Success(requestTokenDto)
        coEvery { authDataSource.validateToken(any(), any(), any()) } returns Result.Success(requestTokenDto)
        coEvery { authDataSource.createSession(requestTokenDto) } returns Result.Error(networkError)
        val result = authRepository.login("username", "password")
        Assert.assertTrue(result is Result.Error)
        Assert.assertEquals(networkError, (result as Result.Error).error)
    }

    @Test
    fun login_fails_because_of_unsuccessful_session_creation() = runBlocking {
        val requestTokenDto = RequestTokenDto(success = true, "", "")
        coEvery { authDataSource.getRequestToken() } returns Result.Success(requestTokenDto)
        coEvery { authDataSource.validateToken(any(), any(), any()) } returns Result.Success(requestTokenDto)
        coEvery { authDataSource.createSession(requestTokenDto) } returns Result.Success(SessionDto(success = false, sessionId = ""))
        val result = authRepository.login("username", "password")
        Assert.assertTrue(result is Result.Error)
        Assert.assertEquals(AuthError.SESSION_CREATION_ERROR, (result as Result.Error).error)
    }
}