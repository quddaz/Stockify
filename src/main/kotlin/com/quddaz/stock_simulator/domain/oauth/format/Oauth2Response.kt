package com.quddaz.stock_simulator.domain.oauth.format

import com.quddaz.stock_simulator.domain.user.entity.SocialType
import com.quddaz.stock_simulator.domain.user.entity.User

interface Oauth2Response {
    fun getName(): String
    fun getEmail(): String
    fun getProviderId(): String
    fun getProvider(): SocialType
    fun toUser(): User
}