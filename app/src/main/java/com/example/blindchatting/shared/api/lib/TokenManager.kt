package com.example.blindchatting.shared.api.lib

import android.content.Context

class TokenManager(private val context: Context) {
    private val accessTokenName = "access_token"
    private val refreshTokenName = "refresh_token"

    private val sharedPreferences = context.getSharedPreferences("AuthPreferences", Context.MODE_PRIVATE)

    fun saveTokenPair(accessToken: String, refreshToken: String) {
        sharedPreferences.edit().putString(accessTokenName, accessToken).apply()
        sharedPreferences.edit().putString(refreshTokenName, refreshToken).apply()
    }

    fun getAccessToken(): String? {
        return sharedPreferences.getString(accessTokenName, null)
    }

    fun getRefreshToken(): String? {
        return sharedPreferences.getString(refreshTokenName, null)
    }

    fun clearTokenPair() {
        sharedPreferences.edit().remove(accessTokenName).apply()
        sharedPreferences.edit().remove(refreshTokenName).apply()
    }

    fun clearAccessToken() {
        sharedPreferences.edit().remove(accessTokenName).apply()
    }

    fun clearRefreshToken() {
        sharedPreferences.edit().remove(refreshTokenName).apply()
    }
}
