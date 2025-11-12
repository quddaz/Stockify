package com.quddaz.stock_simulator.global.config.scheduler

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.TaskScheduler
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler

@Configuration
class StockitySchedulerConfig {

    @Bean("stockityScheduler")
    fun stockityScheduler(): TaskScheduler {
        val scheduler = ThreadPoolTaskScheduler()
        scheduler.poolSize = 1 // 단일 스레드 → 순서 및 단일 실행 보장
        scheduler.initialize()
        return scheduler
    }
}