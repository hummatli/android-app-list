package com.challenge.app.repository.remote

import com.challenge.app.models.Airline
import com.challenge.app.repository.remote.api.WebServiceApi
import com.google.common.truth.Truth.assertThat
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.mobline.data.enqueueResponse
import com.mobline.data.enqueueResponse400
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import java.io.IOException
import java.util.concurrent.TimeUnit

class RepositoryImplTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var client: OkHttpClient
    private lateinit var api: WebServiceApi
    private lateinit var repo: Repository

    @ExperimentalSerializationApi
    @Before
    fun setUp() {
        mockWebServer = MockWebServer()

        client = OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.SECONDS)
            .readTimeout(1, TimeUnit.SECONDS)
            .writeTimeout(1, TimeUnit.SECONDS)
            .build()

        api = Retrofit.Builder()
            .client(client)
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(
                Json {
                    isLenient = true
                    encodeDefaults = true
                    ignoreUnknownKeys = true
                    prettyPrint = true
                }.asConverterFactory("application/json".toMediaType())
            )
            .build()
            .create(WebServiceApi::class.java)

        repo = RepositoryImpl(api)
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        mockWebServer.shutdown()
    }


    @Test
    fun `on response 200 returns success and is equal to predefined list`() {
        mockWebServer.enqueueResponse("response-200.json", 200)

        runBlocking {
            val response = repo.getAirlines()

            assertThat(response).isEqualTo(
                Response.Success(
                    listOf(
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
                )
            )
        }
    }

    @Test
    fun `on response 200 returns success and is not equal to predefined list`() {
        mockWebServer.enqueueResponse("response-200.json", 200)

        runBlocking {
            val response = repo.getAirlines()

            assertThat(response).isNotEqualTo(
                Response.Success(
                    listOf(
                        Airline(
                            site = "https://subus.cl/",
                            code = "SUBUS",
                            phone = "",
                            name = "SuBús",
                            logoURL = "/rimg/provider-logos/airlines/v/SUBUS.png?crop=false&width=108&height=92&fallback=default1.png&_v=prod-ad1150fe671c602861ca3e25a012e0d6ec20ccc4"
                        )
                    )
                )
            )
        }
    }


    @Test
    fun `on response 400 returns error is true`() {
        mockWebServer.enqueueResponse400()

        runBlocking {
            val response = repo.getAirlines()
            assertThat(response is Response.Error).isTrue()
        }
    }

    @Test
    fun `on response 400 returns success is false`() {
        mockWebServer.enqueueResponse400()

        runBlocking {
            val response = repo.getAirlines()
            assertThat(response is Response.Success).isFalse()
        }
    }
}