package com.quddaz.stock_simulator.domain.position.service

import com.quddaz.stock_simulator.domain.position.dto.UserRankingDTO
import com.quddaz.stock_simulator.domain.position.repository.UserPositionRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.CachePut
import org.springframework.context.event.EventListener
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

    @Transactional
    fun removeAllPositions() {
        userPositionRepository.deleteAll()
    }

    @Transactional
    fun updateRankingTop10(defaultMoney: Long = 10_000_000L): List<UserRankingDTO> {
        val rankings = userPositionRepository.findRankings(defaultMoney).rankings

        // 캐시에 저장
        val cache = cacheManager.getCache(rankingCacheName)
        cache?.put(rankingCacheKey, rankings)

        return rankings
    }

    /**
     * 서비스 초기화 시 캐시 세팅
     * ApplicationReadyEvent를 통해 애플리케이션 시작 후 호출 가능
     */
    @EventListener(ApplicationReadyEvent::class)
    fun initRankingCache() {
        updateRankingTop10() // 초기값 세팅
    }
}
