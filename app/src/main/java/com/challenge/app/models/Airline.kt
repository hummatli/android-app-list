package com.challenge.app.models

import android.os.Parcelable
import com.challenge.app.BuildConfig
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Airline(
    val site: String? = null,
    val code: String,
    val alliance: String? = null,
    val phone: String? = null,
    val name: String,
    val usName: String? = null,
    val defaultName: String? = null,
    val logoURL: String
) : Parcelable {

    val fullLogoURL: String
        get() = "${BuildConfig.BASE_API_URL}/$logoURL"
}
