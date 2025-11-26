package com.example.lvlup.data

import android.content.Context
import android.content.SharedPreferences

class AuthDataStore(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    companion object {
        const val KEY_TOKEN = "jwt_token"
        const val KEY_ROLE = "user_role"
        const val KEY_USER_ID = "user_id"
    }

    fun saveAuthData(token: String, userId: Long, role: String) {
        prefs.edit().apply {
            putString(KEY_TOKEN, token)
            putLong(KEY_USER_ID, userId)
            putString(KEY_ROLE, role)
            apply()
        }
    }

    fun getToken(): String? {
        return prefs.getString(KEY_TOKEN, null)
    }

    fun getUserId(): Long {
        return prefs.getLong(KEY_USER_ID, -1L)
    }

    fun getRole(): String? {
        return prefs.getString(KEY_ROLE, null)
    }

    fun clearAuthData() {
        prefs.edit().clear().apply()
    }
}
