package com.quddaz.stock_simulator.domain.user.exception

import com.quddaz.stock_simulator.global.exception.errorcode.ErrorCode

class UserDomainException(
    val errorCode: ErrorCode
) : RuntimeException(errorCode.message)

