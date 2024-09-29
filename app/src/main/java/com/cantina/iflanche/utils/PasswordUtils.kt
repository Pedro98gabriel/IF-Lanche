package com.cantina.iflanche.utils

import at.favre.lib.crypto.bcrypt.BCrypt
import com.cantina.iflanche.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object PasswordUtils {
    private const val BCRYPT_SALT: String = BuildConfig.BCRYPT_SALT

    suspend fun hashPassword(password: String): String {
        return withContext(Dispatchers.IO) {
            BCrypt.with(BCrypt.Version.VERSION_2A)
                .hashToString(8, (password + BCRYPT_SALT).toCharArray())
        }
    }
}