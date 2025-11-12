package com.quddaz.stock_simulator.global.security

import com.quddaz.stock_simulator.domain.oauth.entity.CustomOAuth2User
import com.quddaz.stock_simulator.domain.user.entity.User
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class AuthenticationMaker {
    fun makeAuthentication(user: User) {
        val customOAuth2User = CustomOAuth2User.from(user)

        SecurityContextHolder.getContext().authentication =
            UsernamePasswordAuthenticationToken(
                customOAuth2User,
                null,
                customOAuth2User.authorities
            )
    }
}