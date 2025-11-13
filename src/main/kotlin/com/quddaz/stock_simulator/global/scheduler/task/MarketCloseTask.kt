package com.quddaz.stock_simulator.global.scheduler.task

import com.quddaz.stock_simulator.domain.tradeHistory.service.TradeHistoryService
import com.quddaz.stock_simulator.domain.user.service.UserService
import com.quddaz.stock_simulator.global.log.Loggable
import com.quddaz.stock_simulator.global.scheduler.PrioritizedTask
import com.quddaz.stock_simulator.global.scheduler.TaskGroup
import org.hibernate.query.sqm.tree.SqmNode.log
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Component
@Order(1)
class MarketCloseTask(
    private val tradeHistoryService: TradeHistoryService,
    private val userService: UserService,
    @Value("\${data.initial-user-money}")
    private val initialUserMoney: Long
) : PrioritizedTask, Loggable {

    override val mainTask: TaskGroup = TaskGroup.MARKET_CLOSE

    override val taskGroup: List<TaskGroup> = listOf(TaskGroup.MARKET_CLOSE)


    override fun canExecute(time: java.time.LocalDateTime): Boolean {
        return time.minute == 0 && time.hour % 2 == 0
    }

    override fun execute() {
        tradeHistoryService.getRankingTop10FromCache()
        userService.resetAllUserMoney(initialUserMoney)
        log.info("MarketCloseTask start executing")
    }
}