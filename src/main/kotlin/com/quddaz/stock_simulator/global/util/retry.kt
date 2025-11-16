package com.quddaz.stock_simulator.global.util

import com.quddaz.stock_simulator.global.exception.SchedulerTaskException
import com.quddaz.stock_simulator.global.exception.errorcode.SchedulerTaskErrorCode


/**
 * 재시도 헬퍼 함수
 *
 * @param times 재시도 횟수 (기본 3회)
 * @param delayMs 재시도 간 지연 시간(ms)
 * @param block 재시도할 코드 블록
 */
fun <T> retry(times: Int = 3, delayMs: Long = 1000, block: () -> T): T {
    var lastException: Exception? = null
    repeat(times) { attempt ->
        try {
            return block()
        } catch (e: Exception) {
            lastException = e
            Thread.sleep(delayMs)
        }
    }
    throw SchedulerTaskException(SchedulerTaskErrorCode.SCHEDULER_RETRY_LIMIT_EXCEEDED)
        .apply { initCause(lastException) }
}