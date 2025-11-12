package com.quddaz.stock_simulator.global.scheduler

import java.time.LocalDateTime

interface PrioritizedTask {
    fun canExecute(time: LocalDateTime): Boolean

    fun execute()
}