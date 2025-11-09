package com.quddaz.stock_simulator.domain.oauth.domain

import com.quddaz.stock_simulator.domain.user.entity.Role
import com.quddaz.stock_simulator.domain.user.entity.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.core.user.OAuth2User


class CustomOAuth2User(
    val id: Long,
    val socialId: String,
    val email: String,
    val username: String,
    val role: Role
) : OAuth2User {

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return setOf(SimpleGrantedAuthority(role.value))
    }

    override fun getAttributes(): Map<String, Any> {
        return mapOf(
            "id" to id,
            "socialId" to socialId,
            "email" to email,
            "name" to username,
            "role" to role
        )
    }

    override fun getName(): String {
        return id.toString()
    }

    // 디버깅용
    override fun toString(): String {
        return "CustomOAuth2User(id=$id, email='$email', name='$username', role=$role)"
    }

    companion object {
        fun from(user: User): CustomOAuth2User {
            return CustomOAuth2User(
                id = user.id ?: throw IllegalStateException("User ID가 null입니다."),
                socialId = user.socialId,
                email = user.email,
                username = user.name,
                role = user.role // User의 role 필드
            )
        }
    }
}