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
        log.info("스케줄러 실행 $now")

        // 현재 시간에 실행 가능한 Task 필터링
        val executableTasks = tasks.filter { it.canExecute(now) }
        if (executableTasks.isEmpty()) return

        // 첫 번째로 히트한 Task의 메인 그룹
        val mainGroup = executableTasks.first().task
        log.info("실행되는 메인 테스크 그룹: $mainGroup")

        // 같은 메인 그룹에 속한 Task만 실행
        val tasksToRun = executableTasks.filter { it.task == mainGroup }

        tasksToRun.forEach { task ->
            try {
                log.info("테스크 실행: ${task.task}")
                task.execute()
            } catch (e: Exception) {
                log.error("스케줄러 테스크 실행 에러 ${task.task}", e)
            }
        }
    }
}