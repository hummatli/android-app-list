package com.challenge.app.flow.main

import com.challenge.app.models.Airline
import com.challenge.app.repository.remote.Repository
import com.challenge.app.repository.remote.Response

class FakeRepositoryImpl(val response: Response<List<Airline>>): Repository {
    override suspend fun getAirlines(): Response<List<Airline>> {
        return response
    }
}