package com.example.rumble.infrastructure.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiExecutor {

    const val Rumble_APIS_ENDPOINT = "https://us-central1-rumble-coding-challenge.cloudfunctions.net/api/"

    private fun getRetrofit(endpointURL: String): Retrofit {

        val clientBuilder = OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })

        val okHttpClient = clientBuilder.build()

        return Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(endpointURL)
            .build()
    }

    fun createApi(endpointURL: String): RumbleApi {
        val retrofit = getRetrofit(endpointURL)
        return retrofit.create(RumbleApi::class.java)
    }
}