package com.challenge.app.repository.local

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Before
import org.junit.Test
import com.google.common.truth.Truth.assertThat
import org.junit.After

class AppSettingsImplTest {

    private lateinit var appSettings: AppSettings
    private lateinit var context: Context

    @Before
    fun setUp(){
        context = ApplicationProvider.getApplicationContext()
        appSettings = AppSettingsImpl(context)
    }

    @After
    fun tearDown() {
        appSettings.clear()
    }

    @Test
    fun getValueFromUndefinedKey_withDefaultFalse_returnsFalse() {
        val result = appSettings.getBoolean("sample_key", false)
        assertThat(result).isFalse()
    }

    @Test
    fun getValueFromUndefinedKey_withDefaultTrue_returnsTrue() {
        val result = appSettings.getBoolean("sample_key", true)
        assertThat(result).isTrue()
    }

    @Test
    fun putValueTrue_andGetValueWithTheSameKey_withDefaultValueFalse_isTrue() {
        appSettings.putBoolean("sample_key", true)
        val result = appSettings.getBoolean("sample_key", false)
        assertThat(result).isTrue()
    }

    @Test
    fun putValueFalse_andGetValueWithTheSameKey_withDefaultValueTrue_isFalse() {
        appSettings.putBoolean("sample_key", false)
        val result = appSettings.getBoolean("sample_key", true)
        assertThat(result).isFalse()
    }
}