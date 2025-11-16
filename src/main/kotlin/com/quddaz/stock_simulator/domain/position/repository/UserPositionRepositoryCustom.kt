package com.quddaz.stock_simulator.domain.position.repository

import com.quddaz.stock_simulator.domain.position.dto.PortfolioResponse
import com.quddaz.stock_simulator.domain.position.dto.UserRankingResponse
import com.quddaz.stock_simulator.domain.position.entitiy.UserPosition

interface UserPositionRepositoryCustom {
    fun findPortfolioByUser(userId: Long): PortfolioResponse

    fun findRankings(defaultMoney: Long): UserRankingResponse

    fun findByUserIdAndCompanyIdForUpdate(userId: Long, companyId: Long): UserPosition?
}