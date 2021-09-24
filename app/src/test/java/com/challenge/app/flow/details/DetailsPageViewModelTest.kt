package com.challenge.app.flow.details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.challenge.app.extensions.getOrAwaitValueTest
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class DetailsPageViewModelTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: DetailsPageViewModel

    @Before
    fun start() {
        viewModel = DetailsPageViewModel(FakeAppSettingsImpl())
    }

    @Test
    fun `first call of init favorite, should set favoriteLiveData to false, returns false`() {
        viewModel.initFavorite("sample_key")
        assertThat(viewModel.favoriteLiveData.getOrAwaitValueTest()).isFalse()
    }


    @Test
    fun `one time toggle should make favorite data equal to true, returns true`() {
        viewModel.toggleFavorite("sample_key")
        assertThat(viewModel.favoriteLiveData.getOrAwaitValueTest()).isTrue()
    }

    @Test
    fun `two times toggle should make favorite data equal to false, returns false`() {
        viewModel.toggleFavorite("sample_key")
        viewModel.toggleFavorite("sample_key")
        assertThat(viewModel.favoriteLiveData.getOrAwaitValueTest()).isFalse()
    }
}