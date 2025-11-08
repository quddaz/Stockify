package com.quddaz.stock_simulator.domain.oauth.format

import com.quddaz.stock_simulator.domain.user.entity.Role
import com.quddaz.stock_simulator.domain.user.entity.SocialType
import com.quddaz.stock_simulator.domain.user.entity.User

data class GoogleResponse(
    val attributes: Map<String, Any>
) : Oauth2Response {

    override fun getName() = attributes["name"] as? String ?: "Unknown"
    override fun getEmail() = attributes["email"] as? String ?: "unknown@example.com"
    override fun getProviderId() = attributes["sub"] as? String ?: ""
    override fun getProvider() = SocialType.GOOGLE

    override fun toUser() = User(
        name = getName(),
        email = getEmail(),
        socialType = getProvider(),
        socialId = getProviderId(),
        role = Role.USER,
        money = 10000000L // 초기 자본금 설정
    )
}