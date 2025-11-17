package com.quddaz.stock_simulator.domain.trade.dto

data class TradeResult(
    val stockId: Long,
    val type: String, // "BUY" or "SELL"
    val quantity: Long,
    val price: Double,
    val success: Boolean,
    val message: String? = null
)