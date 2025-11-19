package com.quddaz.stock_simulator.domain.eventHistory.repository

import com.quddaz.stock_simulator.domain.eventHistory.dto.StockChartDataResponse
import com.quddaz.stock_simulator.domain.eventHistory.entity.EventHistory

interface EventHistoryRepositoryCustom {
    fun findChartDataByCompanyName(companyName: String): List<StockChartDataResponse>?
    fun findLatestByCompanyName(companyName: String): Long?
}