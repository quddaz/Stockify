package com.quddaz.stock_simulator.global.exception.errorcode

import org.springframework.http.HttpStatus

interface ErrorCode {
    val httpStatus: HttpStatus
    val message: String
}