package com.quddaz.stock_simulator.domain.trade.service

import com.quddaz.stock_simulator.domain.trade.dto.TradeEvent
import com.quddaz.stock_simulator.global.util.task.TaskGroup
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class TradeProducer(
    private val rabbitTemplate: RabbitTemplate,
    @Value("\${trade.queue.name}") private val queueName: String
) {
    fun send(event: TradeEvent) {
        rabbitTemplate.convertAndSend(queueName, event)
    }

    fun sendBuy(userId: Long, stockId: Long, quantity: Long, price: Long) {
        send(TradeEvent.BuyEvent(userId, stockId, quantity, price))
    }

    fun sendSell(userId: Long, stockId: Long, quantity: Long, price: Long) {
        send(TradeEvent.SellEvent(userId, stockId, quantity, price))
    }

    fun sendScheduler(taskMainGroup: TaskGroup) {
        send(TradeEvent.SchedulerEvent(taskMainGroup))
    }
}