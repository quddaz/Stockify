package com.quddaz.stock_simulator.global.exception.errorcode

import org.springframework.http.HttpStatus

enum class GlobalErrorCode(
    override val httpStatus: HttpStatus,
    override val code: String
) : ErrorCode {

    // 공통 에러
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR"),
    RUNTIME_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "RUNTIME_ERROR"),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR"),

    // 요청 관련 에러
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "BAD_REQUEST"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "NOT_FOUND");
}