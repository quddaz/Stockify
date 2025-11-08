package com.quddaz.stock_simulator.global.security.handler

import com.fasterxml.jackson.databind.ObjectMapper
import com.quddaz.stock_simulator.global.response.ResponseTemplate
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component

/**
 * AccessToken 은 있으나 권한이 맞지 않는 경우 - 403
 */
@Component
class JwtAccessDeniedHandler(
    private val objectMapper: ObjectMapper
) : AccessDeniedHandler {
    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: org.springframework.security.access.AccessDeniedException
    ) {
        response.status = HttpStatus.FORBIDDEN.value()
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = "UTF-8"

        objectMapper.writeValue(response.writer, ResponseTemplate.ROLE_ERROR)
    }
}