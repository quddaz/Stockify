package com.quddaz.stock_simulator.global.config.jwt

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jwt")
data class JwtProperties(
    val secretKey: String,
    val issuer: String,
    val accessTokenExpiration: Long,
    val refreshTokenExpiration: Long,

    val scheme: String,
    val format: String,
    val header: String,
    val name: String
)