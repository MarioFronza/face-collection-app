package com.mariofronza.face_collection_app.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.mariofronza.face_collection_app.R
import com.mariofronza.face_collection_app.models.User


class SessionManager(context: Context) {

    private var prefs: SharedPreferences =
        context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)


    companion object {
        const val USER_DATA = "user_data"
        const val USER_TOKEN = "user_token"
        const val REFRESH_TOKEN = "refresh_token"
    }

    fun saveAuthToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    fun saveRefreshToken(token: String) {
        val editor = prefs.edit()
        editor.putString(REFRESH_TOKEN, token)
        editor.apply()
    }

    fun saveUserData(user: User) {
        val editor = prefs.edit()
        val converter = Gson()
        val json = converter.toJson(user)
        editor.putString(USER_DATA, json)
        editor.apply()
    }

    fun removeTokens() {
        val editor = prefs.edit()
        editor.remove(USER_TOKEN)
        editor.remove(REFRESH_TOKEN)
        editor.remove(USER_DATA)
        editor.apply()
    }

    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    fun fetchRefreshToken(): String? {
        return prefs.getString(REFRESH_TOKEN, null)
    }

    fun fetchUserData(): User? {
        val converter = Gson()
        return converter.fromJson(prefs.getString(USER_DATA, ""), User::class.java)
    }
}