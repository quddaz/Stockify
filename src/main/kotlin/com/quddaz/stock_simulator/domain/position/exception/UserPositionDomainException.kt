package com.quddaz.stock_simulator.domain.position.exception

import com.quddaz.stock_simulator.global.exception.errorcode.ErrorCode

class UserPositionDomainException(
    val errorCode: ErrorCode
) : RuntimeException(errorCode.message)