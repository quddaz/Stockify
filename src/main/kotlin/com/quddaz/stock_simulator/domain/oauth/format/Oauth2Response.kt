package com.quddaz.stock_simulator.domain.oauth.format

import com.quddaz.stock_simulator.domain.user.entity.SocialType

interface Oauth2Response {
    fun getName(): String
    fun getEmail(): String
    fun getProviderId(): String
    fun getProvider(): SocialType
}