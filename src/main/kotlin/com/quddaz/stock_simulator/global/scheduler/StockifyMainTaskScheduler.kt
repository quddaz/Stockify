package com.quddaz.stock_simulator.global.scheduler

import com.quddaz.stock_simulator.global.exception.SchedulerTaskException
import com.quddaz.stock_simulator.global.exception.errorcode.SchedulerTaskErrorCode
import com.quddaz.stock_simulator.global.log.Loggable
import com.quddaz.stock_simulator.global.scheduler.task.TaskExecutor
import com.quddaz.stock_simulator.global.scheduler.task.TaskSelector
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class StockifyMainTaskScheduler(
    private val taskSelector: TaskSelector, // 테스크 선택기
    private val taskExecutor: TaskExecutor, // 테스크 실행기
    private val stockUpdatePublisher: StockUpdatePublisher // 주식 업데이트 퍼블리셔(WebSocket)
) : Loggable {

    @Scheduled(cron = "0 */5 * * * *")
    fun runScheduledTasks() {
        val now = LocalDateTime.now()
        log.info("스케줄러 실행 $now")

        try {
            val executableTasks = taskSelector.filterExecutableTasks(now)
            if (executableTasks.isEmpty()) return

            val mainGroup = taskSelector.getMainGroup(executableTasks)
            log.info("실행되는 메인 테스크 그룹: $mainGroup")

            taskExecutor.executeGroupTasks(executableTasks, mainGroup)
            stockUpdatePublisher.publishStockUpdate()
            stockUpdatePublisher.publishMarketClose(mainGroup)

        } catch (e: Exception) {
            log.error("스케줄러 실행 중 예외 발생", e)
            throw SchedulerTaskException(SchedulerTaskErrorCode.SCHEDULER_TASK_EXECUTION_FAILED).apply { initCause(e) }
        }
    }
}