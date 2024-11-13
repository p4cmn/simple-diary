package com.artem.records.utils

import java.security.MessageDigest

object PasswordUtils {

    fun hashPassword(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    fun checkPassword(password: String, hashedPassword: String): Boolean {
        return hashPassword(password) == hashedPassword
    }

}
