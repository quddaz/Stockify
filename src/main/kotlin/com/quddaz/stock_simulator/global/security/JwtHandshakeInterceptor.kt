package com.quddaz.stock_simulator.global.security

import com.quddaz.stock_simulator.domain.oauth.entity.CustomOAuth2User
import com.quddaz.stock_simulator.domain.oauth.service.JwtTokenProvider
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.http.server.ServletServerHttpRequest
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.context.SecurityContextHolderThreadLocalAccessor
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.server.HandshakeInterceptor

@Component
class JwtHandshakeInterceptor(private val jwtProvider: JwtTokenProvider) : HandshakeInterceptor {

    override fun beforeHandshake(
        request: ServerHttpRequest, response: ServerHttpResponse,
        wsHandler: WebSocketHandler, attributes: MutableMap<String, Any>
    ): Boolean {
        val auth = SecurityContextHolder.getContext().authentication
        val customOAuth2User = auth.principal as CustomOAuth2User

        attributes["userId"] = customOAuth2User.id
        return true
    }

    override fun afterHandshake(
        request: ServerHttpRequest, response: ServerHttpResponse,
        wsHandler: WebSocketHandler, ex: Exception?
    ) {}
}
