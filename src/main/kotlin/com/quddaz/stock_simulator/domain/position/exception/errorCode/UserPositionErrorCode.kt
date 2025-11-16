package com.quddaz.stock_simulator.domain.position.exception.errorCode

import com.quddaz.stock_simulator.global.exception.errorcode.ErrorCode
import org.springframework.http.HttpStatus

enum class UserPositionErrorCode(
    override val httpStatus: HttpStatus,
    override val message: String
): ErrorCode {
    POSITION_NOT_FOUND(HttpStatus.NOT_FOUND, "NOT FOUND POSITION."),
    INSUFFICIENT_SHARES(HttpStatus.BAD_REQUEST, "INSUFFICIENT SHARES TO SELL."),
}