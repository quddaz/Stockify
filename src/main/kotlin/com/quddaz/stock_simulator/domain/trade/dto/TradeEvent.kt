package com.quddaz.stock_simulator.domain.trade.dto

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.quddaz.stock_simulator.global.util.task.TaskGroup

sealed class TradeEvent {
    data class BuyEvent(
        val userId: Long,
        val companyName: String,
        val quantity: Long,
        val price: Long
    ) : TradeEvent()

    data class SellEvent(
        val userId: Long,
        val companyName: String,
        val quantity: Long,
        val price: Long
    ) : TradeEvent()

    data class SchedulerEvent(
        val taskMainGroup: TaskGroup
    ) : TradeEvent()
}
