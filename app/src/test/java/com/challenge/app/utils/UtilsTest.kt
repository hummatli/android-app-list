package com.challenge.app.utils

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class UtilsTest {

    @Test
    fun `adds http at the start if input does not have and returns true`() {
        val result = Utils.correctWebUrl("test.com")
        assertThat(result).isEqualTo("http://test.com")
    }

    @Test
    fun `does not add http if input have and returns true`() {
        val result = Utils.correctWebUrl("http://test.com")
        assertThat(result).isEqualTo("http://test.com")
    }

    @Test
    fun `does not add https if input have and return true`() {
        val result = Utils.correctWebUrl("https://test.com")
        assertThat(result).isEqualTo("https://test.com")
    }

}