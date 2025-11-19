package com.quddaz.stock_simulator.domain.position.dto

data class PortfolioDTO(
    val companyId: Long,
    val companyName: String,
    val quantity: Long, // 보유 수량
    val averagePrice: Long, // 평균 매입가
    val currentPrice: Long, // 현재가
    val evaluationAmount: Long, // 평가금액
    val unrealizedPL: Long // 미실현 손익
)
