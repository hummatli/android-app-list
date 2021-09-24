package com.challenge.app.repository.remote

import com.challenge.app.models.Airline

interface Repository {

    suspend fun getAirlines(): Response<List<Airline>>
}
