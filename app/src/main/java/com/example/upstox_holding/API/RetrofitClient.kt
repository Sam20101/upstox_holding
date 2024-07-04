package com.example.upstox_holding.API

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://35dee773a9ec441e9f38d5fc249406ce.api.mockbin.io/"

    val instance: UpstoxAPIService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(UpstoxAPIService::class.java)
    }
}