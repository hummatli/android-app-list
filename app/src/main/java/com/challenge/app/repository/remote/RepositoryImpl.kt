package com.challenge.app.repository.remote

import com.challenge.app.models.Airline
import com.challenge.app.repository.remote.api.WebServiceApi

class RepositoryImpl(private val api: WebServiceApi) : Repository {

    override suspend fun getAirlines(): Response<List<Airline>> {
        return try {
            Response.Success(api.getAirlines().map { it.toDomain() })
        } catch (exception: Exception) {
            Response.Error(exception)
        }
    }
}

sealed class Response<T> {
    data class Success<T>(val result: T): Response<T>()
    data class Error<T>(val exception: Exception): Response<T>()
}
