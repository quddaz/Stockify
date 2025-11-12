package com.quddaz.stock_simulator.global.scheduler

import com.quddaz.stock_simulator.global.log.Loggable
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class StockifyMainTaskScheduler(
    private val tasks: List<PrioritizedTask>,
) : Loggable {

    @Scheduled(cron = "0 */5 * * * *")
    fun runScheduledTasks() {
        val now = LocalDateTime.now()
        log.info("Scheduled task executed at $now")

        for (task in tasks) {
            if (task.canExecute(now)) {
                task.execute()
            }
        }
    }
}