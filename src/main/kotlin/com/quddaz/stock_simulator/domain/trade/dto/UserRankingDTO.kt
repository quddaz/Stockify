package com.quddaz.stock_simulator.domain.trade.dto

data class UserRankingDTO(
    val username: String,
    val totalAssets: Long,
    val profitRate: Double
)