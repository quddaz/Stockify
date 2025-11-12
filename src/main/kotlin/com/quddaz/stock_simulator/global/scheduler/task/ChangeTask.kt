package com.quddaz.stock_simulator.global.scheduler.task

import com.quddaz.stock_simulator.global.scheduler.PrioritizedTask
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Component
@Order(4)
class ChangeTask : PrioritizedTask {
    override fun canExecute(time: java.time.LocalDateTime): Boolean {
        return time.minute % 5 == 0
    }

    override fun execute() {
        //TODO 변화 작업 구현
    }
}