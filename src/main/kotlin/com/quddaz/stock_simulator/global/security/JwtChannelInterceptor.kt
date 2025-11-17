package com.quddaz.stock_simulator.global.security

import com.quddaz.stock_simulator.domain.oauth.service.JwtTokenProvider
import com.quddaz.stock_simulator.domain.user.entity.User
import com.quddaz.stock_simulator.global.config.jwt.JwtProperties
import com.quddaz.stock_simulator.global.log.Loggable
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.messaging.support.MessageHeaderAccessor
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component


@Component
class JwtChannelInterceptor(
    private val jwtTokenProvider: JwtTokenProvider,
    private val authenticationMaker: AuthenticationMaker,
    private val jwtProperties: JwtProperties
) : ChannelInterceptor, Loggable {


    override fun preSend(message: Message<*>, channel: MessageChannel): Message<*> {
        val accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor::class.java)
            ?: return message

        if (StompCommand.CONNECT == accessor.command) {
            val token = accessor.getFirstNativeHeader(jwtProperties.header)
                ?.takeIf { it.startsWith(jwtProperties.scheme) }
                ?: throw IllegalArgumentException("JWT Token is missing or invalid")

            val jwt = token.substring(7) // "Bearer " 제거
            val user: User = jwtTokenProvider.getUser(jwt) // JWT 검증 & 사용자 조회

            // SecurityContext에 인증 정보 생성
            authenticationMaker.makeAuthentication(user)
            accessor.user = SecurityContextHolder.getContext().authentication
            log.info("WebSocket Security Context에 '{}' 인증 정보를 저장했습니다", accessor.user)
        }

        return message
    }

}
