package com.example.upstox_holding.Repository

import android.annotation.SuppressLint
import com.example.upstox_holding.API.UpstoxAPIService
import com.example.upstox_holding.Model.HoldingResponseData
import com.example.upstox_holding.Model.UserHoldingModel
import java.lang.Exception

class UserHoldingRepository(private val upstoxAPIService: UpstoxAPIService) {
    @SuppressLint("SuspiciousIndentation")
    suspend fun getUserHoldings(): List<UserHoldingModel> {
    val responseData = upstoxAPIService.getHoldingStocks()
        if(responseData.isSuccessful)
        {
            return responseData.body()?.holdingResponseData?.userHoldingModelList?: emptyList()
        }else
        {
            throw Exception("API call failed with code ${responseData.code()}")
        }
    }
}