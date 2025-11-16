package com.quddaz.stock_simulator.domain.company.exception

import com.quddaz.stock_simulator.global.exception.errorcode.ErrorCode

class CompanyDomainException(
    val errorCode : ErrorCode
) : RuntimeException(errorCode.message)