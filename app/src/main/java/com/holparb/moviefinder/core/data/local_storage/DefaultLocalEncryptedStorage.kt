package com.holparb.moviefinder.core.data.local_storage

import android.content.Context
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.holparb.moviefinder.core.domain.util.LocalEncryptedStorage
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DefaultLocalEncryptedStorage @Inject constructor(
    @ApplicationContext private val context: Context
): LocalEncryptedStorage {

    companion object {
        private const val PREFERENCE_NAME = "local_encrypted_pref"
        private const val SESSION_ID = "session_id"
        private const val USER_ID = "user_id"
    }

    private val masterKey by lazy {
        MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
    }

    private val encryptedSharedPreferences by lazy {
        EncryptedSharedPreferences.create(
            context,
            PREFERENCE_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    override suspend fun saveSessionId(sessionId: String) {
        encryptedSharedPreferences.edit {
            putString(SESSION_ID, sessionId)
        }
    }

    override fun getSessionId(): String? {
        return encryptedSharedPreferences.getString(SESSION_ID, null)
    }

    override suspend fun saveUserId(userId: Int) {
        encryptedSharedPreferences.edit {
            putInt(USER_ID, userId)
        }
    }

    override fun getUserId(): Int {
        return encryptedSharedPreferences.getInt(USER_ID, 0)
    }
}