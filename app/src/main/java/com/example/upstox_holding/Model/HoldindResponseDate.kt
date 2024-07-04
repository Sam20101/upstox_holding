package com.example.upstox_holding.Model

import com.google.gson.annotations.SerializedName

data class HoldingResponseData(
    @SerializedName("data")
    val holdingResponseData: HoldingResponseList
)