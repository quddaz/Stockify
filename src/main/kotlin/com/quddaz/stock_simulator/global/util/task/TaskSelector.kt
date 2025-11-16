package com.quddaz.stock_simulator.global.util.task

import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class TaskSelector(private val tasks: List<PrioritizedTask>) {

    fun filterExecutableTasks(time: LocalDateTime): List<PrioritizedTask> =
        tasks.filter { it.canExecute(time) }

    fun getMainGroup(executableTasks: List<PrioritizedTask>): TaskGroup =
        executableTasks.first().mainTask

    fun getAllTasks(): List<PrioritizedTask> = tasks
}