package com.quddaz.stock_simulator.domain.trade.service

import com.quddaz.stock_simulator.domain.trade.dto.TradeEvent
import com.quddaz.stock_simulator.global.log.Loggable
import com.quddaz.stock_simulator.global.util.StockUpdatePublisher
import com.quddaz.stock_simulator.global.util.task.TaskExecutor
import com.quddaz.stock_simulator.global.util.task.TaskSelector
import org.hibernate.query.sqm.tree.SqmNode.log
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TradeConsumer(
    private val tradeService: TradeService,         // Buy/Sell 처리
    private val taskSelector: TaskSelector,         // Scheduler Task 선택
    private val taskExecutor: TaskExecutor,         // Scheduler Task 실행
    private val stockUpdatePublisher: StockUpdatePublisher // WebSocket 퍼블리시
) : Loggable {

    @RabbitListener(queues = ["\${trade.queue.name}"])
    fun consume(event: TradeEvent) {
        try {
            when (event) {
                is TradeEvent.BuyEvent -> handleBuy(event)
                is TradeEvent.SellEvent -> handleSell(event)
                is TradeEvent.SchedulerEvent -> handleScheduler(event)
            }
        } catch (e: Exception) {
            log.error("메시지 처리 중 알 수 없는 에러 발생: ${e.message}", e)

            if (event is TradeEvent.BuyEvent) {
                stockUpdatePublisher.publishTradeError(event.userId, e.message ?: "매수 중 시스템 오류가 발생했습니다.")
            } else if (event is TradeEvent.SellEvent) {
                stockUpdatePublisher.publishTradeError(event.userId, e.message ?: "매도 중 시스템 오류가 발생했습니다.")
            }
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
