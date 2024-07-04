package com.example.upstox_holding.Model

data class UserHoldingModel(
    val avgPrice: Double,
    val close: Double,
    val ltp: Double,
    val quantity: Int,
    val symbol: String
)