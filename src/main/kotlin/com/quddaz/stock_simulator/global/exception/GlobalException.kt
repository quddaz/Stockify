package com.quddaz.stock_simulator.global.exception

import com.quddaz.stock_simulator.global.exception.errorcode.ErrorCode

class GlobalException(
    val errorCode: ErrorCode
) : RuntimeException()
