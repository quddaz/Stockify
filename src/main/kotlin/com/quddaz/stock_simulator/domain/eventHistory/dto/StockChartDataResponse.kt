package com.quddaz.stock_simulator.domain.eventHistory.dto

import java.time.LocalDateTime

data class StockChartDataResponse(
    val time: LocalDateTime,
    val price: Long
)