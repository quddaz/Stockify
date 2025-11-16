package com.quddaz.stock_simulator.global.scheduler.task

import com.quddaz.stock_simulator.global.log.Loggable
import com.quddaz.stock_simulator.global.util.retry
import org.springframework.stereotype.Component

@Component
class TaskExecutor : Loggable {

    fun executeGroupTasks(executableTasks: List<PrioritizedTask>, mainGroup: TaskGroup) {
        executableTasks
            .filter { it.contains(mainGroup) }
            .forEach { task ->
                retry(times = 3, delayMs = 1000) {
                    log.info("테스크 실행: ${task.mainTask}")
                    task.execute()
                }
            }
    }

}
