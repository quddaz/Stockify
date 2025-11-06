package com.quddaz.stock_simulator.global.exception

import com.quddaz.stock_simulator.global.response.ResponseTemplate
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(ex: MethodArgumentNotValidException): ResponseEntity<ResponseTemplate<Any>> {
        val errors = ex.bindingResult.fieldErrors.associate { it.field to (it.defaultMessage ?: "잘못된 입력") }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ResponseTemplate.fail("유효성 검증 실패", "VALIDATION_ERROR", errors))
    }

    @ExceptionHandler(RuntimeException::class)
    fun handleRuntimeException(ex: RuntimeException): ResponseEntity<ResponseTemplate<Any>> {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ResponseTemplate.fail(ex.message ?: "서버 내부 오류", "RUNTIME_ERROR"))
    }

    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception): ResponseEntity<ResponseTemplate<Any>> {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ResponseTemplate.fail(ex.message ?: "예기치 못한 오류가 발생했습니다.", "INTERNAL_ERROR"))
    }
}