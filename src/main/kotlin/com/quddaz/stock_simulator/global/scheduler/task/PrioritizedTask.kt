package com.quddaz.stock_simulator.global.scheduler.task

import java.time.LocalDateTime

interface PrioritizedTask {
    val mainTask: TaskGroup                 // 대표 그룹
    val taskGroup: List<TaskGroup>      // 함께 실행 가능한 그룹 목록

    fun canExecute(time: LocalDateTime): Boolean
    fun execute()

    fun getMainTaskGroup(): TaskGroup = mainTask
    fun contains(group: TaskGroup): Boolean = taskGroup.contains(group)
}