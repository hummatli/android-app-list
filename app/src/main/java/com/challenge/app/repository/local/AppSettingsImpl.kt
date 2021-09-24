package com.challenge.app.repository.local

import android.content.Context
import android.content.SharedPreferences

class AppSettingsImpl(context: Context): AppSettings {

    private val pref: SharedPreferences = context.getSharedPreferences("KayakLocal", Context.MODE_PRIVATE)

    override fun putBoolean(key: String, value: Boolean) {
        pref.edit().putBoolean(key, value).apply()
    }

    override fun getBoolean(key: String, default: Boolean) = pref.getBoolean(key, default)

    override fun clear() {
        pref.edit().clear().apply()
    }
}