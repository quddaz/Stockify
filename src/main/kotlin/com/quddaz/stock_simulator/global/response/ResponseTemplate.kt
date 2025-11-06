package com.quddaz.stock_simulator.global.response

import com.quddaz.stock_simulator.global.exception.errorcode.ErrorCode

data class ResponseTemplate<T>(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val results: T? = null
) {
    companion object {
        fun <T> success(data: T): ResponseTemplate<T> =
            ResponseTemplate(true, "200", "SUCCESS", data)

        fun <T> fail(errorCode: ErrorCode, data: T? = null): ResponseTemplate<T> =
            ResponseTemplate(false, errorCode.httpStatus.toString() , errorCode.message, data)
    }
}