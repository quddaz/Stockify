package com.quddaz.stock_simulator.domain.company.dto

import com.quddaz.stock_simulator.domain.eventHistory.dto.StockChartDataResponse

data class CompanyPageResponse(
    val companyName: String,
    val currentPrice: Long,
    val totalShares: Long,
    val sector: String,
    val description: String,

    val riskMetrics: RiskMetrics,

    val chart: List<StockChartDataResponse>
)