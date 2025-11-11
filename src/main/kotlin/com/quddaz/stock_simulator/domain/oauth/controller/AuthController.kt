package com.quddaz.stock_simulator.domain.oauth.controller

import com.quddaz.stock_simulator.domain.oauth.service.AuthService
import com.quddaz.stock_simulator.global.config.jwt.JwtProperties
import com.quddaz.stock_simulator.global.response.ResponseTemplate
import io.swagger.v3.oas.annotations.Operation
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService,
    private val jwtProperties: JwtProperties
) {

    @GetMapping("/reissue")
    @Operation(summary = "토큰 재발급", description = "리프레시 토큰을 받아 액세스 토큰을 재발급합니다")
    fun reIssueToken(
        @CookieValue(name = "REFRESH_TOKEN", required = false) refreshToken: String?,
        response: HttpServletResponse
    ): ResponseEntity<ResponseTemplate<*>> {

        val tokenResponse = authService.reissueAccessToken(refreshToken)

        response.addCookie(tokenResponse.refreshToken)
        response.setHeader(jwtProperties.header, "${jwtProperties.scheme} ${tokenResponse.accessToken}")

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ResponseTemplate.UNIT_SUCCESS)
    }

    @GetMapping("/{id}")
    @Operation(summary = "테스트 토큰 발급", description = "userId를 받아 테스트 토큰을 발급합니다")
    fun test(@PathVariable id: Long): ResponseTemplate<String> {
        val token = authService.generateAccessToken(id)
        return ResponseTemplate.success(token)
    }
}
