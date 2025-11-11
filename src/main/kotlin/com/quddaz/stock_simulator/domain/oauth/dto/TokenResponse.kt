package com.quddaz.stock_simulator.domain.oauth.dto

import jakarta.servlet.http.Cookie

data class TokenResponse (
    val accessToken: String,
    val refreshToken: Cookie
)