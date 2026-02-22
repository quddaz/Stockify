package com.quddaz.stock_simulator.global.util.task

import com.quddaz.stock_simulator.global.log.Loggable
import org.springframework.retry.support.RetryTemplate
import org.springframework.stereotype.Component

@Component
class TaskExecutor : Loggable {

    private val retryTemplate: RetryTemplate = RetryTemplate.builder()
        .maxAttempts(3)
        .fixedBackoff(1000)
        .retryOn(Exception::class.java)
        .build()

    fun executeGroupTasks(executableTasks: List<PrioritizedTask>, mainGroup: TaskGroup) {
        executableTasks
            .filter { it.contains(mainGroup) }
            .forEach { task ->
                retryTemplate.execute<Unit, Exception> {
                    log.info("테스크 실행: ${task.mainTask}")
                    task.execute()
                }
            }
    }
}
