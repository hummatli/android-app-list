package com.challenge.app.flow.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.challenge.app.extensions.CoroutineRule
import com.challenge.app.extensions.getOrAwaitValueTest
import com.challenge.app.models.Airline
import com.challenge.app.repository.remote.Response
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import java.lang.Exception


class MainPageViewModelTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesTestRule = CoroutineRule()

    private lateinit var viewModel: MainPageViewModel


    @ExperimentalCoroutinesApi
    @Test
    fun `get airlines from repo with success case, state is equal Loaded with the same input`() {
        val input = listOf(
            Airline(
                site = "https://subus.cl/",
                code = "SUBUS",
                phone = "",
                name = "SuBús",
                logoURL = "/rimg/provider-logos/airlines/v/SUBUS.png?crop=false&width=108&height=92&fallback=default1.png&_v=prod-ad1150fe671c602861ca3e25a012e0d6ec20ccc4"
            ),
            Airline(
                site = "sitasudtrasporti.it",
                code = "SITASUD",
                phone = "",
                name = "SITA Sud",
                logoURL = "/rimg/provider-logos/airlines/v/SITASUD.png?crop=false&width=108&height=92&fallback=default3.png&_v=8e634b2affd75b43a2cb1f32d857ccc2"
            )
        )

        val repo = FakeRepositoryImpl(Response.Success(input))
        viewModel = MainPageViewModel(repo)


        viewModel.getAirlines()

        coroutinesTestRule.pauseDispatcher()

        assertThat(viewModel.listStateLiveData.getOrAwaitValueTest()).isEqualTo(ListState.Loaded(input))

        coroutinesTestRule.resumeDispatcher()
    }


    @ExperimentalCoroutinesApi
    @Test
    fun `get airlines from repo with error case, state is equal Error in view model`() {

        val ex = Exception("sample ex")
        val repo = FakeRepositoryImpl(Response.Error(ex))
        viewModel = MainPageViewModel(repo)


        viewModel.getAirlines()

        coroutinesTestRule.pauseDispatcher()

        assertThat(viewModel.listStateLiveData.getOrAwaitValueTest()).isEqualTo(ListState.Error(ex))

        coroutinesTestRule.resumeDispatcher()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `get airlines from repo with error case, effect is equal to ShowErrorMessage in view model`() {

        val ex = Exception("sample ex")
        val repo = FakeRepositoryImpl(Response.Error(ex))
        viewModel = MainPageViewModel(repo)


        viewModel.getAirlines()

        coroutinesTestRule.pauseDispatcher()

        assertThat(viewModel.effect.getOrAwaitValueTest()).isEqualTo(Effect.ShowErrorMessage(ex))

        coroutinesTestRule.resumeDispatcher()
    }


}