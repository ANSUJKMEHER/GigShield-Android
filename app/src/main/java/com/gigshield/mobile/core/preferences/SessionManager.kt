package com.gigshield.mobile.core.preferences

import android.content.Context
import android.content.SharedPreferences
import com.gigshield.mobile.core.common.Constants
import com.gigshield.mobile.data.model.UserDto
import com.google.gson.Gson

class SessionManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveToken(token: String?) {
        prefs.edit().putString(Constants.KEY_JWT_TOKEN, token).apply()
    }

    fun getToken(): String? {
        return prefs.getString(Constants.KEY_JWT_TOKEN, null)
    }

    fun saveUserId(userId: String) {
        prefs.edit().putString(Constants.KEY_USER_ID, userId).apply()
    }

    fun getUserId(): String? {
        return prefs.getString(Constants.KEY_USER_ID, null)
    }

    fun saveUser(user: UserDto) {
        prefs.edit()
            .putString(Constants.KEY_USER_ID, user.id)
            .putString("cached_user_json", gson.toJson(user))
            .apply()
    }

    fun getCachedUser(): UserDto? {
        val json = prefs.getString("cached_user_json", null) ?: return null
        return try {
            gson.fromJson(json, UserDto::class.java)
        } catch (e: Exception) {
            null
        }
    }

    fun getServerMode(): String {
        return prefs.getString(Constants.KEY_SERVER_MODE, Constants.MODE_CLOUD) ?: Constants.MODE_CLOUD
    }

    fun setServerMode(mode: String) {
        prefs.edit().putString(Constants.KEY_SERVER_MODE, mode).apply()
    }

    fun getCustomUrl(): String? {
        return prefs.getString(Constants.KEY_CUSTOM_URL, null)
    }

    fun setCustomUrl(url: String) {
        prefs.edit().putString(Constants.KEY_CUSTOM_URL, url).apply()
    }

    fun getEffectiveBaseUrl(): String {
        return when (getServerMode()) {
            Constants.MODE_EMULATOR -> Constants.DEFAULT_EMULATOR_URL
            Constants.MODE_DEMO_OFFLINE -> Constants.DEFAULT_PROD_URL // routed through mock client
            else -> getCustomUrl()?.takeIf { it.isNotBlank() } ?: Constants.DEFAULT_PROD_URL
        }
    }

    fun isDarkMode(): Boolean {
        return prefs.getBoolean(Constants.KEY_DARK_MODE, true)
    }

    fun setDarkMode(dark: Boolean) {
        prefs.edit().putBoolean(Constants.KEY_DARK_MODE, dark).apply()
    }

    fun clearSession() {
        prefs.edit()
            .remove(Constants.KEY_JWT_TOKEN)
            .remove(Constants.KEY_USER_ID)
            .remove("cached_user_json")
            .apply()
    }
}
