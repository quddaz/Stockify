package com.quddaz.stock_simulator.global.security.handler

import com.quddaz.stock_simulator.domain.oauth.domain.CustomOAuth2User
import com.quddaz.stock_simulator.domain.oauth.service.JwtTokenProvider
import com.quddaz.stock_simulator.global.config.jwt.JwtProperties
import com.quddaz.stock_simulator.global.exception.GlobalException
import com.quddaz.stock_simulator.global.exception.errorcode.GlobalErrorCode
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component

@Component
class CustomOAuth2SuccessHandler(
    private val jwtTokenProvider: JwtTokenProvider,
    private val jwtProperties: JwtProperties,
    @Value("\${oauth2.redirect-uri}") private val redirectUri: String
) : SimpleUrlAuthenticationSuccessHandler(){

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val customOAuth2User = authentication.principal as CustomOAuth2User

        val accessToken = jwtTokenProvider.createAccessToken(
            userId = customOAuth2User.id,
            role = customOAuth2User.role.toString()
        )

        val refreshTokenCookie = jwtTokenProvider.createRefreshToken(
            userId = customOAuth2User.id,
            role = customOAuth2User.role.toString()
        )

        response.addCookie(refreshTokenCookie)
        response.setHeader(jwtProperties.header, "${jwtProperties.scheme} $accessToken")
        response.sendRedirect(redirectUri)
    }
}