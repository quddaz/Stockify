package com.quddaz.stock_simulator.domain.oauth.format

import com.quddaz.stock_simulator.domain.user.entity.SocialType

data class GoogleResponse(
    val attributes: Map<String, Any>
) : Oauth2Response {

    override fun getName(): String {
        return attributes["name"] as String
    }

    override fun getEmail(): String {
        return attributes["email"] as String
    }

    override fun getProviderId(): String {
        return attributes["sub"] as String
    }

    override fun getProvider(): SocialType {
        return SocialType.GOOGLE
    }
}