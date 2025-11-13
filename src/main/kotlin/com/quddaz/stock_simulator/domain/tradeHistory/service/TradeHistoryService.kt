package com.quddaz.stock_simulator.domain.tradeHistory.service

import com.quddaz.stock_simulator.domain.tradeHistory.dto.PortfolioResponse
import com.quddaz.stock_simulator.domain.tradeHistory.dto.UserRankingDTO
import com.quddaz.stock_simulator.domain.tradeHistory.repository.TradeHistoryRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class TradeHistoryService(
    private val cacheManager: CacheManager,
    private val tradeHistoryRepository: TradeHistoryRepository,
    @Value("\${cache.ranking.name}")
    private val rankingCacheName: String,

    @Value("\${cache.ranking.key}")
    private val rankingCacheKey: String
) {
    /** 포트폴리오 조회 */
    fun getPortfolioByUser(userId: Long): PortfolioResponse {
        val portfolio = tradeHistoryRepository.findPortfolioByUser(userId)
        return PortfolioResponse(portfolio)
    }

    /** 랭킹 Top 10 조회 */
    fun getRankingTop10FromCache(): List<UserRankingDTO>? {
        val cache = cacheManager.getCache(rankingCacheName) ?: return null
        return cache.get(rankingCacheKey)?.get() as? List<UserRankingDTO>
    }
    @CachePut("rankingTop10")
    fun updateRankingTop10(defaultMoney: Long) = tradeHistoryRepository.findRankingTop10(defaultMoney)
}