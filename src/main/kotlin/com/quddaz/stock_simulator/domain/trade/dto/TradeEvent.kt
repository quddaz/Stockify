package com.quddaz.stock_simulator.domain.trade.dto

import com.quddaz.stock_simulator.global.util.task.TaskGroup

sealed class TradeEvent {
    data class BuyEvent(
        val userId: Long,
        val stockId: Long,
        val quantity: Long,
        val price: Long
    ) : TradeEvent()

    data class SellEvent(
        val userId: Long,
        val stockId: Long,
        val quantity: Long,
        val price: Long
    ) : TradeEvent()

    data class SchedulerEvent(
        val taskMainGroup: TaskGroup
    ) : TradeEvent()
}
