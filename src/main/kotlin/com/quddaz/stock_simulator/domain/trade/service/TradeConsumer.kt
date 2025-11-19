package com.quddaz.stock_simulator.domain.trade.service

import com.quddaz.stock_simulator.domain.trade.dto.TradeEvent
import com.quddaz.stock_simulator.global.util.StockUpdatePublisher
import com.quddaz.stock_simulator.global.util.task.TaskExecutor
import com.quddaz.stock_simulator.global.util.task.TaskSelector
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TradeConsumer(
    private val tradeService: TradeService,         // Buy/Sell 처리
    private val taskSelector: TaskSelector,         // Scheduler Task 선택
    private val taskExecutor: TaskExecutor,         // Scheduler Task 실행
    private val stockUpdatePublisher: StockUpdatePublisher // WebSocket 퍼블리시
) {

    @RabbitListener(queues = ["\${trade.queue.name}"])
    fun consume(event: TradeEvent) {
        when (event) {
            is TradeEvent.BuyEvent -> handleBuy(event)
            is TradeEvent.SellEvent -> handleSell(event)
            is TradeEvent.SchedulerEvent -> handleScheduler(event)
        }
    }

    private fun handleBuy(event: TradeEvent.BuyEvent) {
        tradeService.buy(event.userId, event.companyName, event.quantity)
        stockUpdatePublisher.publishTradeUpdate(event.userId, event)
    }

    private fun handleSell(event: TradeEvent.SellEvent) {
        tradeService.sell(event.userId, event.companyName, event.quantity, event.price)
        stockUpdatePublisher.publishTradeUpdate(event.userId, event)
    }

    private fun handleScheduler(event: TradeEvent.SchedulerEvent) {
        val tasks = taskSelector.getAllTasks()


        taskExecutor.executeGroupTasks(tasks, event.taskMainGroup)

        // WebSocket 퍼블리시
        stockUpdatePublisher.publishStockUpdate()
        stockUpdatePublisher.publishMarketClose(event.taskMainGroup)
    }
}
