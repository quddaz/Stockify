package com.quddaz.stock_simulator.global.scheduler.task

import com.quddaz.stock_simulator.global.scheduler.PrioritizedTask
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Component
@Order(3)
class EventTask : PrioritizedTask {
    override fun canExecute(time: java.time.LocalDateTime): Boolean {
        return time.minute % 15 == 0
    }

    override fun execute() {
        //TODO 이벤트 발생 작업 구현
    }
}