package com.quddaz.stock_simulator.global.scheduler

import com.quddaz.stock_simulator.domain.trade.service.TradeProducer
import com.quddaz.stock_simulator.global.log.Loggable
import com.quddaz.stock_simulator.global.util.task.TaskSelector
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class StockifyMainTaskScheduler(
    private val taskSelector: TaskSelector, // 테스크 선택기
    private val tradeProducer: TradeProducer  // 테스크를 큐에 전송
) : Loggable {

    @Scheduled(cron = "0 */5 * * * *")
    fun runScheduledTasks() {
        val now = LocalDateTime.now()
        log.info("스케줄러 실행 $now")

        val executableTasks = taskSelector.filterExecutableTasks(now)
        if (executableTasks.isEmpty()) return

        val mainGroup = taskSelector.getMainGroup(executableTasks)
        tradeProducer.sendScheduler(mainGroup)
    }
}