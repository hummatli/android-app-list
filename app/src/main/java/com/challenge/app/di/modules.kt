package com.challenge.app.di

import com.challenge.app.BuildConfig
import com.challenge.app.repository.local.AppSettingsImpl
import com.challenge.app.extensions.disableSSLVerification
import com.challenge.app.flow.details.DetailsPageViewModel
import com.challenge.app.flow.main.MainPageViewModel
import com.challenge.app.repository.local.AppSettings
import com.challenge.app.repository.remote.Repository
import com.challenge.app.repository.remote.RepositoryImpl
import com.challenge.app.repository.remote.api.WebServiceApi
import com.challenge.app.utils.TimberInitializer
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

//////////////////////////////////// Repo module //////////////////////////
val repositoryModule: Module = module {

    //Network
    single {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
            else HttpLoggingInterceptor.Level.NONE
        )
    }

    single {
        val builder = OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .retryOnConnectionFailure(false)
            .callTimeout(120, TimeUnit.SECONDS)
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) builder.disableSSLVerification()

        builder.build()
    }

    single {
        Json {
            isLenient = true
            encodeDefaults = true
            ignoreUnknownKeys = true
            prettyPrint = true
        }
    }

    single<Retrofit> {
        Retrofit.Builder()
            .client(get())
            .baseUrl(BuildConfig.BASE_API_URL)
            .addConverterFactory(get<Json>().asConverterFactory("application/json".toMediaType()))
            .build()
    }


    single<WebServiceApi> {
        get<Retrofit>().create(WebServiceApi::class.java)
    }


    single<Repository> {
        RepositoryImpl(
            get()
        )
    }

    single<AppSettings> {
        AppSettingsImpl(androidContext())
    }

    //Initializers
    single { TimberInitializer() }

}

//////////////////////////////////// View Models ////////////////////////////////////
val viewModelsModule = module {
    viewModel {
        MainPageViewModel(repository = get())
    }

    viewModel {
        DetailsPageViewModel(appSettings = get())
    }
}