package com.example.upstox_holding.API

import com.example.upstox_holding.Model.HoldingResponseData
import retrofit2.Response
import retrofit2.http.GET

interface UpstoxAPIService {
    @GET("/")
    suspend fun getHoldingStocks(): Response<HoldingResponseData>

}