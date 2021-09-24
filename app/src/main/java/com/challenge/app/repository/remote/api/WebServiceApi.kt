package com.challenge.app.repository.remote.api

import com.challenge.app.models.remote.AirlineResponse
import retrofit2.http.GET

interface WebServiceApi {
    @GET("/h/mobileapis/directory/airlines")
    suspend fun getAirlines(): List<AirlineResponse>
}
