package com.quddaz.stock_simulator.domain.trade.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull

data class TradeSellRequest(
    @field:NotNull
    val companyName: String,

    @field:Min(1, message = "수량은 1 이상이어야 합니다.")
    val quantity: Long
)
