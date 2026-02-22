package com.quddaz.stock_simulator.domain.trade.service

import com.quddaz.stock_simulator.domain.trade.dto.TradeEvent
import org.springframework.amqp.ImmediateRequeueAmqpException
import com.quddaz.stock_simulator.global.log.Loggable
import com.quddaz.stock_simulator.global.util.StockUpdatePublisher
import com.quddaz.stock_simulator.global.util.task.TaskExecutor
import com.quddaz.stock_simulator.global.util.task.TaskSelector
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service

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
        } catch (e: IllegalArgumentException) {
            log.warn("유효하지 않은 요청으로 메시지 처리 실패: ${e.message}")
            publishTradeError(event, e.message ?: "잘못된 요청입니다.")
        } catch (e: Exception) {
            log.error("메시지 처리 중 시스템 에러 발생: ${e.message}", e)
            publishTradeError(event, "일시적 오류가 발생했습니다. 재시도됩니다.")
            throw ImmediateRequeueAmqpException("메시지 처리 재시도", e)
        }
    }

    private fun handleBuy(event: TradeEvent.BuyEvent) {
        tradeService.buy(event.userId, event.companyName, event.quantity)
        stockUpdatePublisher.publishTradeUpdate(event.userId, event)
    }

    private fun handleSell(event: TradeEvent.SellEvent) {
        tradeService.sell(event.userId, event.companyName, event.quantity)
        stockUpdatePublisher.publishTradeUpdate(event.userId, event)
    }

    private fun publishTradeError(event: TradeEvent, message: String) {
        when (event) {
            is TradeEvent.BuyEvent -> stockUpdatePublisher.publishTradeError(event.userId, message)
            is TradeEvent.SellEvent -> stockUpdatePublisher.publishTradeError(event.userId, message)
            is TradeEvent.SchedulerEvent -> Unit
        }
    }

    private fun handleScheduler(event: TradeEvent.SchedulerEvent) {
        val tasks = taskSelector.getAllTasks()


        taskExecutor.executeGroupTasks(tasks, event.taskMainGroup)

        // WebSocket 퍼블리시
        stockUpdatePublisher.publishStockUpdate()
        stockUpdatePublisher.publishMarketClose(event.taskMainGroup)
    }
}
