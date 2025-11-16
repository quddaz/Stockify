package com.quddaz.stock_simulator.domain.position.service

import com.quddaz.stock_simulator.domain.position.dto.UserRankingDTO
import com.quddaz.stock_simulator.domain.position.repository.UserPositionRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.CachePut
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserPositionService(
    private val userPositionRepository: UserPositionRepository,
    private val cacheManager: CacheManager,

    @Value("\${cache.ranking.name}")
    private val rankingCacheName: String,

    @Value("\${cache.ranking.key}")
    private val rankingCacheKey: String
) {

    fun getPortfolioByUser(userId: Long) =
        userPositionRepository.findPortfolioByUser(userId)

    /** 랭킹 Top 10 조회 */
    fun getRankingTop10FromCache(): List<UserRankingDTO>? {
        val cache = cacheManager.getCache(rankingCacheName) ?: return null
        return cache.get(rankingCacheKey)?.get() as? List<UserRankingDTO>
    }

    /** 장마감 시 랭킹 계산 후 캐시에 저장 */
    @CachePut(cacheNames = ["rankingTop10"], key = "'ranking'")
    fun updateRankingTop10(defaultMoney: Long = 10_000_000L): List<UserRankingDTO> {
        return userPositionRepository.findRankings(defaultMoney).rankings
    }


}