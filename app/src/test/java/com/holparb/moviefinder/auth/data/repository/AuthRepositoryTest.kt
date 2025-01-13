package com.holparb.moviefinder.auth.data.repository

import com.holparb.moviefinder.auth.data.datasource.AuthDataSource
import com.holparb.moviefinder.auth.domain.repository.AuthRepository
import com.holparb.moviefinder.core.domain.util.LocalEncryptedStorage
import io.mockk.mockk
import org.junit.Before

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


}