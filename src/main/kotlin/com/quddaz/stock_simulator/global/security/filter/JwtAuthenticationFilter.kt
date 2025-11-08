package com.quddaz.stock_simulator.global.security.filter

import com.quddaz.stock_simulator.domain.oauth.service.JwtTokenProvider
import com.quddaz.stock_simulator.domain.user.entity.User
import com.quddaz.stock_simulator.global.config.jwt.JwtProperties
import com.quddaz.stock_simulator.global.security.AuthenticationMaker
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtTokenProvider: JwtTokenProvider,
    private val authenticationMaker: AuthenticationMaker,
    private val jwtProperties: JwtProperties
) : OncePerRequestFilter() {

    companion object {
        private val log = LoggerFactory.getLogger(JwtAuthenticationFilter::class.java)
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        resolveToken(request)?.takeIf { jwtTokenProvider.validateToken(it) }?.let { token ->
            val user: User = jwtTokenProvider.getUser(token)
            authenticationMaker.makeAuthentication(user)
            log.info("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", user.id, request.requestURI)
        }

        filterChain.doFilter(request, response)
    }

    private fun resolveToken(request: HttpServletRequest): String? =
        request.getHeader(jwtProperties.header)
            ?.takeIf { it.startsWith(jwtProperties.scheme) }
            ?.removePrefix(jwtProperties.scheme)
}