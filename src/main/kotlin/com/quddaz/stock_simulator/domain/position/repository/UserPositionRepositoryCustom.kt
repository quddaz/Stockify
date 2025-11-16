package com.quddaz.stock_simulator.domain.position.repository

import com.quddaz.stock_simulator.domain.position.dto.PortfolioResponse
import com.quddaz.stock_simulator.domain.position.dto.UserRankingResponse

interface UserPositionRepositoryCustom {
    fun findPortfolioByUser(userId: Long): PortfolioResponse

    fun findRankings(defaultMoney: Long): UserRankingResponse
}