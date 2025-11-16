package com.quddaz.stock_simulator.domain.trade.dto

data class TradeSellRequest(
    val companyId: Long,
    val quantity: Long,
    val price: Long
)