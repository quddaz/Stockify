package com.quddaz.stock_simulator.global.scheduler

import java.time.LocalDateTime

interface PrioritizedTask {
    val task: TaskGroup                 // 대표 그룹
    val taskGroup: List<TaskGroup>      // 함께 실행 가능한 그룹 목록

    fun canExecute(time: LocalDateTime): Boolean
    fun execute()

    fun getMainTaskGroup(): TaskGroup = task
    fun contains(group: TaskGroup): Boolean = taskGroup.contains(group)
}