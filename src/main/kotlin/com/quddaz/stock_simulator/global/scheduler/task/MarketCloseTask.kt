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

    private val DEFAULT_MONEY = 10_000_000L
    override fun canExecute(time: java.time.LocalDateTime): Boolean {
        return time.minute == 0 && time.hour % 2 == 0
    }

    override fun execute() {
        val rankings = tradeHistoryService.getRankingTop10(DEFAULT_MONEY)
        //TODO 로컬 캐쉬 또는 DB에 저장

        userService.resetAllUserMoney(DEFAULT_MONEY)

        log.info("MarketCloseTask start executing")
    }
}