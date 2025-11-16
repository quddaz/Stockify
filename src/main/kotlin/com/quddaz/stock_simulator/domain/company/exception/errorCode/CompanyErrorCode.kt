package com.quddaz.stock_simulator.domain.company.exception.errorCode

import com.quddaz.stock_simulator.global.exception.errorcode.ErrorCode
import org.springframework.http.HttpStatus

enum class CompanyErrorCode(
    override val httpStatus : HttpStatus,
    override val message: String
) : ErrorCode {
    NOT_ENOUGH_SHARES(HttpStatus.BAD_REQUEST, "NOT_ENOUGH_SHARES"),



}