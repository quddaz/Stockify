package com.quddaz.stock_simulator.domain.company.dto


data class CompanyStockInfoDTO(
    val companyName: String,
    val previousPrice: Long, // 전일 종가
    val currentPrice: Long, // 현재가
    val changeRate: Double, // 등락률
    val sector: String  // 섹터 이름
)