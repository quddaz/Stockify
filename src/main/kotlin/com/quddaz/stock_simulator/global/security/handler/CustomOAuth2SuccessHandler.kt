package com.quddaz.stock_simulator.global.security.handler

import com.quddaz.stock_simulator.domain.oauth.entity.CustomOAuth2User
import com.quddaz.stock_simulator.domain.oauth.service.JwtTokenProvider
import com.quddaz.stock_simulator.global.config.jwt.JwtProperties
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component

@Component
class CustomOAuth2SuccessHandler(
    private val jwtTokenProvider: JwtTokenProvider,
    private val jwtProperties: JwtProperties,
) : SimpleUrlAuthenticationSuccessHandler() {

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val customOAuth2User = authentication.principal as CustomOAuth2User

        val accessToken = jwtTokenProvider.createAccessToken(customOAuth2User.id, customOAuth2User.role)

        val refreshTokenCookie = jwtTokenProvider.createRefreshToken(customOAuth2User.id, customOAuth2User.role)

        response.addCookie(refreshTokenCookie)
        response.setHeader(jwtProperties.header, "${jwtProperties.scheme} $accessToken")
        response.sendRedirect("http://localhost:3000/login/callback?accessToken=$accessToken")
    }
}