package com.quddaz.stock_simulator.domain.position.dto

import com.quddaz.stock_simulator.domain.user.dto.UserDto

data class PortfolioResponse(
    val positions: List<PortfolioDTO>,
    val user: UserDto
)