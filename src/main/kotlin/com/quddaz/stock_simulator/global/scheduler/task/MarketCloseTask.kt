package com.quddaz.stock_simulator.global.scheduler.task

import com.quddaz.stock_simulator.global.log.Loggable
import com.quddaz.stock_simulator.global.scheduler.PrioritizedTask
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Component
@Order(1)
class MarketCloseTask : PrioritizedTask, Loggable {

    override fun canExecute(time: java.time.LocalDateTime): Boolean {
        // 매 2시간마다 정각에 실행
        return time.minute == 0 && time.hour % 2 == 0
    }

    override fun execute() {
        //TODO 시장 마감 작업 구현(랭킹, 사용자 초기 자금 초기화)
    }
}