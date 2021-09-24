package com.challenge.app.models.remote

import com.challenge.app.models.Airline
import kotlinx.serialization.Serializable

@Serializable
data class AirlineResponse(
    val site: String,
    val code: String,
    val alliance: String,
    val phone: String,
    val name: String,
    val usName: String,
    val defaultName: String,
    val logoURL: String
) {
    fun toDomain() = Airline(
        code = code,
        name = name,
        logoURL = logoURL,
        site = site,
        phone = phone
    )
}
