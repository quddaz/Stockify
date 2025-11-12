package com.quddaz.stock_simulator.global.scheduler.task

import com.quddaz.stock_simulator.domain.tradeHistory.service.TradeHistoryService
import com.quddaz.stock_simulator.domain.user.service.UserService
import com.quddaz.stock_simulator.global.log.Loggable
import com.quddaz.stock_simulator.global.scheduler.PrioritizedTask
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Component
@Order(1)
class MarketCloseTask(
    private val tradeHistoryService: TradeHistoryService,
    private val userService: UserService
) : PrioritizedTask, Loggable {

    override fun canExecute(time: java.time.LocalDateTime): Boolean {
        // 매 2시간마다 정각에 실행
        return time.minute == 0 && time.hour % 2 == 0
    }

    override fun execute() {

    }
}