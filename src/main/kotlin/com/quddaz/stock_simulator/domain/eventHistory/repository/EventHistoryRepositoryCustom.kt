package com.quddaz.stock_simulator.domain.eventHistory.repository

import com.quddaz.stock_simulator.domain.eventHistory.dto.StockChartDataResponse

interface EventHistoryRepositoryCustom {
    fun findChartDataByCompanyName(companyName: String): List<StockChartDataResponse>?
}