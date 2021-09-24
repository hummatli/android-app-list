package com.challenge.app.flow.details

import com.challenge.app.repository.local.AppSettings

class FakeAppSettingsImpl : AppSettings {
    val map = mutableMapOf<String, Boolean>()

    override fun putBoolean(key: String, value: Boolean) {
        map[key] = value
    }

    override fun getBoolean(key: String, default: Boolean): Boolean {
        return map.getOrDefault(key, default)
    }

    override fun clear() {
        map.clear()
    }
}