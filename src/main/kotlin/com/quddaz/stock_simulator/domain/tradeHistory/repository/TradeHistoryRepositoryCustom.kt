package com.quddaz.stock_simulator.domain.tradeHistory.repository

import com.quddaz.stock_simulator.domain.tradeHistory.dto.PortfolioDto
import com.quddaz.stock_simulator.domain.tradeHistory.dto.UserRankingDTO


interface TradeHistoryRepositoryCustom {
    fun findPortfolioByUser(userId: Long): List<PortfolioDto>
    fun findRankingTop10(defaultMoney : Long): List<UserRankingDTO>
}