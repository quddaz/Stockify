package com.quddaz.stock_simulator.domain.oauth.exception

import com.quddaz.stock_simulator.global.exception.errorcode.ErrorCode

class LoginTypeNotSupportException(
    val errorCode: ErrorCode
): RuntimeException(errorCode.message)