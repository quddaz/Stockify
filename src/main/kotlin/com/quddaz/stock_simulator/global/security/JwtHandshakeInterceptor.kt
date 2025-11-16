package com.quddaz.stock_simulator.global.security

import com.quddaz.stock_simulator.domain.oauth.service.JwtTokenProvider
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.http.server.ServletServerHttpRequest
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.server.HandshakeInterceptor

@Component
class JwtHandshakeInterceptor(private val jwtProvider: JwtTokenProvider) : HandshakeInterceptor {

    override fun beforeHandshake(
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        wsHandler: WebSocketHandler,
        attributes: MutableMap<String, Any>
    ): Boolean {
        val token = (request as? ServletServerHttpRequest)?.servletRequest
            ?.getHeader("Authorization")?.removePrefix("Bearer ")
        if (token.isNullOrBlank()) return false

        val userId = try {
            jwtProvider.getUser(token)
        } catch (e: Exception) {
            return false
        }

        attributes["userId"] = userId
        return true
    }

    override fun afterHandshake(
        request: ServerHttpRequest, response: ServerHttpResponse,
        wsHandler: WebSocketHandler, ex: Exception?
    ) {}
}
