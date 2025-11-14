package com.quddaz.stock_simulator.domain.company.dto

data class CompanyStockInfoDTO(
    val companyName: String,
    val previousPrice: Long,
    val currentPrice: Long,
    val changeRate: Double,
    val sector: String
)