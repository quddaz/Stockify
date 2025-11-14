package com.quddaz.stock_simulator.global.exception

import com.quddaz.stock_simulator.global.exception.errorcode.SchedulerTaskErrorCode

class SchedulerTaskException(
    val errorCode: SchedulerTaskErrorCode
) : RuntimeException()