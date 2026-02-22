package com.quddaz.stock_simulator.global.util.task

import com.quddaz.stock_simulator.global.log.Loggable
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Recover
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Component

@Component
class TaskExecutor : Loggable {

    fun executeGroupTasks(executableTasks: List<PrioritizedTask>, mainGroup: TaskGroup) {
        executableTasks
            .filter { it.contains(mainGroup) }
            .forEach { task ->
                executeWithRetry(task)
            }
    }

    @Retryable(maxAttempts = 3, backoff = Backoff(delay = 1000))
    fun executeWithRetry(task: PrioritizedTask) {
        log.info("테스크 실행: ${task.mainTask}")
        task.execute()
    }

    @Recover
    fun recoverTaskExecution(e: Exception, task: PrioritizedTask) {
        log.error("테스크 실행 최종 실패: ${task.mainTask}", e)
        throw e
    }
}
