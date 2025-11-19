package com.quddaz.stock_simulator.domain.company.exception.errorCode

import com.quddaz.stock_simulator.global.exception.errorcode.ErrorCode
import org.springframework.http.HttpStatus

enum class CompanyErrorCode(
    override val httpStatus: HttpStatus,
    override val message: String
) : ErrorCode {
    NOT_ENOUGH_SHARES(HttpStatus.BAD_REQUEST, "살려는 주식 수가 회사의 남은 주식 수보다 많습니다."),
    COMPANY_NOT_FOUND(HttpStatus.NOT_FOUND, "COMPANY_NOT_FOUND")


}