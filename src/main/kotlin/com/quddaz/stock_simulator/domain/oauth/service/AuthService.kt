package com.quddaz.stock_simulator.domain.oauth.service

import com.quddaz.stock_simulator.domain.oauth.dto.TokenResponse
import com.quddaz.stock_simulator.domain.oauth.exception.TokenNotValidException
import com.quddaz.stock_simulator.domain.oauth.exception.errorcode.AuthErrorCode
import com.quddaz.stock_simulator.domain.user.entity.Role
import com.quddaz.stock_simulator.domain.user.exception.UserDomainException
import com.quddaz.stock_simulator.domain.user.exception.errorcode.UserErrorCode
import com.quddaz.stock_simulator.global.config.jwt.JwtProperties
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val jwtTokenProvider: JwtTokenProvider,
    private val jwtProperties: JwtProperties
) {

    fun reissueAccessToken(refreshToken: String?, response: HttpServletResponse): TokenResponse {
        val token = refreshToken ?: throw TokenNotValidException(AuthErrorCode.NOT_EXISTING_OAUTH_TOKEN)

        if (!jwtTokenProvider.validateToken(token)) {
            throw TokenNotValidException(AuthErrorCode.INVALID_OAUTH_TOKEN)
        }

        val user = jwtTokenProvider.getUser(token)
        val userId = user.id ?: throw UserDomainException(UserErrorCode.USER_NOT_FOUND)

        val newAccessToken = jwtTokenProvider.createAccessToken(userId, user.role)
        val newRefreshTokenCookie = jwtTokenProvider.createRefreshToken(userId, user.role)

        return TokenResponse(
            accessToken = newAccessToken,
            refreshToken = newRefreshTokenCookie
        )
    }

    fun generateAccessToken(userId: Long): String {
        return jwtTokenProvider.createAccessToken(userId, Role.USER)
    }
}