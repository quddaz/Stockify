package com.quddaz.stock_simulator.global.util.task

import org.springframework.core.annotation.AnnotationAwareOrderComparator
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class TaskSelector(private val tasks: List<PrioritizedTask>) {

    private val orderedTasks: List<PrioritizedTask> = tasks
        .sortedWith(AnnotationAwareOrderComparator.INSTANCE)

    fun filterExecutableTasks(time: LocalDateTime): List<PrioritizedTask> =
        orderedTasks.filter { it.canExecute(time) }

    fun getMainGroup(executableTasks: List<PrioritizedTask>): TaskGroup =
        executableTasks.first().mainTask

    fun getAllTasks(): List<PrioritizedTask> = orderedTasks
}