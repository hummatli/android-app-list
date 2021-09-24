package com.challenge.app.flow.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.challenge.app.base.BaseViewModel
import com.challenge.app.models.Airline
import com.challenge.app.repository.remote.Repository
import com.challenge.app.repository.remote.Response
import com.challenge.app.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainPageViewModel(
    private val repository: Repository
) : BaseViewModel() {

    private val _listStateLiveData = MutableLiveData<ListState>(ListState.Init)
    val listStateLiveData: LiveData<ListState> = _listStateLiveData

    private val _effect = SingleLiveEvent<Effect>()
    val effect: SingleLiveEvent<Effect> = _effect

    init {
        getAirlines()
    }

    fun getAirlines() {
        _listStateLiveData.value = ListState.Loading

        viewModelScope.launch(Dispatchers.Main) {

            when (val response = repository.getAirlines()) {
                is Response.Success -> {
                    _listStateLiveData.value = ListState.Loaded(response.result)
                }
                is Response.Error -> {
                    _listStateLiveData.value = ListState.Error(response.exception)
                    _effect.value = Effect.ShowErrorMessage(response.exception)
                }
            }
        }
    }
}


sealed class ListState {
    object Loading : ListState()
    object Init : ListState()
    data class Loaded(val result: List<Airline>) : ListState()
    data class Error(val ex: Throwable) : ListState()
}

sealed class Effect {
    data class ShowErrorMessage(val ex: Throwable) : Effect()
}
