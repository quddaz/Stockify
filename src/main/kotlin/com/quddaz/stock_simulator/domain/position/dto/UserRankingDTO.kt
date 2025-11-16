package com.quddaz.stock_simulator.domain.position.dto

data class UserRankingDTO(
    val username: String,
    val totalAssets: Long,
    val profitRate: Double
)