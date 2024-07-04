package com.example.upstox_holding.Model

import com.google.gson.annotations.SerializedName

data class HoldingResponseList(
    @SerializedName("userHolding")
    val userHoldingModelList: List<UserHoldingModel>
)