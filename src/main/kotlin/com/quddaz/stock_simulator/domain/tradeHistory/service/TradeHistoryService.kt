package com.quddaz.stock_simulator.domain.tradeHistory.service

import com.quddaz.stock_simulator.domain.tradeHistory.dto.PortfolioResponse
import com.quddaz.stock_simulator.domain.tradeHistory.repository.TradeHistoryRepository
import org.springframework.stereotype.Service

@Service
class TradeHistoryService(
    private val tradeHistoryRepository: TradeHistoryRepository
) {
    /** 포트폴리오 조회 */
    fun getPortfolioByUser(userId: Long): PortfolioResponse {
        val portfolio = tradeHistoryRepository.findPortfolioByUser(userId)
        return PortfolioResponse(portfolio)
    }

    fun getRankingTop10(defaultMoney: Long) = tradeHistoryRepository.findRankingTop10(defaultMoney)
}