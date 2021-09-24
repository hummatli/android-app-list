package com.challenge.app.flow.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.challenge.app.R
import com.challenge.app.base.BaseViewModel
import com.challenge.app.repository.local.AppSettings

class DetailsPageViewModel(
    private val appSettings: AppSettings,
) : BaseViewModel() {

    private val _favoriteLiveData = MutableLiveData<Boolean>()
    val favoriteLiveData: LiveData<Boolean> = _favoriteLiveData

    fun initFavorite(key: String) {
        _favoriteLiveData.value = appSettings.getBoolean(key, false)
    }

    fun toggleFavorite(key: String) {
        val newValue = _favoriteLiveData.value != true
        _favoriteLiveData.value = newValue
        appSettings.putBoolean(key, newValue)
    }

    fun getFavoriteImage(selected: Boolean) =
        if (selected) R.drawable.ic_star_selected else R.drawable.ic_star_unselected

}
