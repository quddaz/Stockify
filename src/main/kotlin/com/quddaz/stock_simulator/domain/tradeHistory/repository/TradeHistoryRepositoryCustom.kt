package com.quddaz.stock_simulator.domain.tradeHistory.repository

import com.quddaz.stock_simulator.domain.tradeHistory.dto.PortfolioDto


interface TradeHistoryRepositoryCustom {
    fun findPortfolioByUser(userId: Long): List<PortfolioDto>
}