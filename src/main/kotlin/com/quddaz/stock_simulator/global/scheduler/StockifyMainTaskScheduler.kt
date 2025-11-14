package com.quddaz.stock_simulator.global.scheduler

import com.quddaz.stock_simulator.domain.company.service.CompanyPriceService
import com.quddaz.stock_simulator.global.log.Loggable
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class StockifyMainTaskScheduler(
    private val tasks: List<PrioritizedTask>,
    private val companyPriceService: CompanyPriceService
) : Loggable {

    @Scheduled(cron = "0 */5 * * * *")
    fun runScheduledTasks() {
        val now = LocalDateTime.now()
        log.info("스케줄러 실행 $now")

        val executableTasks = filterExecutableTasks(now)
        if (executableTasks.isEmpty()) return

        val mainGroup = getMainGroup(executableTasks)
        log.info("실행되는 메인 테스크 그룹: $mainGroup")

        executeGroupTasks(executableTasks, mainGroup)
        companyPriceService.setCompanyStockInfo()
    }

    private fun filterExecutableTasks(time: LocalDateTime): List<PrioritizedTask> =
        tasks.filter { it.canExecute(time) }

    private fun getMainGroup(executableTasks: List<PrioritizedTask>): TaskGroup =
        executableTasks.first().mainTask

    private fun executeGroupTasks(
        executableTasks: List<PrioritizedTask>,
        mainGroup: TaskGroup
    ) {
        executableTasks
            .filter { it.mainTask == mainGroup }
            .forEach { task ->
                try {
                    log.info("테스크 실행: ${task.mainTask}")
                    task.execute()
                } catch (e: Exception) {
                    log.error("스케줄러 테스크 실행 에러 ${task.mainTask}", e)
                }
            }
    }
}
