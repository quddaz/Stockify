package com.quddaz.stock_simulator.global.exception.errorcode

import org.springframework.http.HttpStatus

enum class SchedulerTaskErrorCode(
    override val httpStatus: HttpStatus,
    override val message: String
) : ErrorCode {

    SCHEDULER_TASK_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SCHEDULER_TASK_ERROR"),
    SCHEDULER_TASK_EXECUTION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "SCHEDULER_TASK_EXECUTION_FAILED"),
    SCHEDULER_RETRY_LIMIT_EXCEEDED(HttpStatus.INTERNAL_SERVER_ERROR, "SCHEDULER_RETRY_LIMIT_EXCEEDED"),
}