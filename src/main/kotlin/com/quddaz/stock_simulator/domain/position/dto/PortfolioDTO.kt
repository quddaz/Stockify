package com.quddaz.stock_simulator.domain.position.dto

data class PortfolioDTO(
    val companyId: Long,
    val companyName: String,
    val quantity: Long,
    val averagePrice: Long,
    val currentPrice: Long,
    val evaluationAmount: Long,
    val unrealizedPL: Long
)
