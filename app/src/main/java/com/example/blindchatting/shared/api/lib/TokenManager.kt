package com.example.blindchatting.shared.api.lib

import android.content.Context

class TokenManager(private val context: Context) {
    private val accessTokenName = "access_token"
    private val refreshTokenName = "refresh_token"
    private val userIdName = "user_id"

    private val sharedPreferences = context.getSharedPreferences("AuthPreferences", Context.MODE_PRIVATE)

    fun saveUserId(userId: Int) {
        sharedPreferences.edit().putInt(userIdName, userId).apply()
    }

    fun saveAccessToken(accessToken: String) {
        sharedPreferences.edit().putString(accessTokenName, accessToken).apply()
    }

    fun saveRefreshToken(refreshToken: String) {
        sharedPreferences.edit().putString(refreshTokenName, refreshToken).apply()
    }

    fun getUserId(): Int {
        return sharedPreferences.getInt(userIdName, -1)
    }

    fun getAccessToken(): String? {
        return sharedPreferences.getString(accessTokenName, null)
    }

    fun getRefreshToken(): String? {
        return sharedPreferences.getString(refreshTokenName, null)
    }

    fun clearTokenPair() {
        sharedPreferences.edit().remove(userIdName).apply()

        sharedPreferences.edit().remove(accessTokenName).apply()
        sharedPreferences.edit().remove(refreshTokenName).apply()
    }
}
