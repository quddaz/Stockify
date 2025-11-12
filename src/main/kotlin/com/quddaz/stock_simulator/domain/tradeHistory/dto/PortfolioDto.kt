package com.quddaz.stock_simulator.domain.tradeHistory.dto

data class PortfolioDto(
    val companyId: Long,
    val companyName: String,
    val shareCount: Long,
    val averagePrice: Long,
    val currentPrice: Long,
    val unrealizedPL: Long // 미실현 손익
)