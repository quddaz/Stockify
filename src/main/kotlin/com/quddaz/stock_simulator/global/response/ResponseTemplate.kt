package com.quddaz.stock_simulator.global.response

import com.quddaz.stock_simulator.global.exception.errorcode.ErrorCode
import org.springframework.http.HttpStatus

data class ResponseTemplate<T>(
    val isSuccess: Boolean,
    val code: Int,
    val message: String,
    val results: T? = null
) {
    companion object {
        fun <T> success(data: T): ResponseTemplate<T> =
            ResponseTemplate(true, HttpStatus.OK.value() , "SUCCESS", data)

        fun <T> fail(errorCode: ErrorCode, data: T? = null): ResponseTemplate<T> =
            ResponseTemplate(false, errorCode.httpStatus.value() , errorCode.message, data)

        val ROLE_ERROR = ResponseTemplate<Unit>(
            isSuccess = false,
            code = HttpStatus.FORBIDDEN.value(),
            message = "NOT AUTHORIZED ROLE",
            results = null
        )

        val AUTHENTICATION_ERROR = ResponseTemplate<Unit>(
            isSuccess = false,
            code = HttpStatus.UNAUTHORIZED.value(),
            message = "NOT AUTHENTICATED USER",
            results = null
        )
    }
}