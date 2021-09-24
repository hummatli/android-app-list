package com.challenge.app.repository.local

interface AppSettings {
    fun putBoolean(key: String, value: Boolean)
    fun getBoolean(key: String, default: Boolean): Boolean
    fun clear()
}