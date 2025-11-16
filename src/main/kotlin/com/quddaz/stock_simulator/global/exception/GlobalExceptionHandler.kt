package com.quddaz.stock_simulator.global.exception

import com.quddaz.stock_simulator.global.exception.errorcode.GlobalErrorCode
import com.quddaz.stock_simulator.global.response.ResponseTemplate
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler


class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(ex: MethodArgumentNotValidException): ResponseEntity<ResponseTemplate<Any>> {
        val errors = ex.bindingResult.fieldErrors.associate { it.field to (it.defaultMessage ?: "잘못된 입력") }
        val errorCode = GlobalErrorCode.VALIDATION_ERROR

        return ResponseEntity
            .status(errorCode.httpStatus)
            .body(ResponseTemplate.fail(errorCode, errors))
    }

    @ExceptionHandler(RuntimeException::class)
    fun handleRuntimeException(ex: RuntimeException): ResponseEntity<ResponseTemplate<Any>> {
        val errorCode = GlobalErrorCode.RUNTIME_ERROR

        return ResponseEntity
            .status(errorCode.httpStatus)
            .body(ResponseTemplate.fail(errorCode, ex.message))
    }

    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception): ResponseEntity<ResponseTemplate<Any>> {
        val errorCode = GlobalErrorCode.INTERNAL_ERROR

        return ResponseEntity
            .status(errorCode.httpStatus)
            .body(ResponseTemplate.fail(errorCode, ex.message))
    }
}