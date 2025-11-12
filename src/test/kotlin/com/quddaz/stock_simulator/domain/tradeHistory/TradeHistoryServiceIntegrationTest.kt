package com.quddaz.stock_simulator.domain.tradeHistory

import com.quddaz.stock_simulator.domain.company.entity.Company
import com.quddaz.stock_simulator.domain.company.entity.Sector
import com.quddaz.stock_simulator.domain.company.repository.CompanyRepository
import com.quddaz.stock_simulator.domain.tradeHistory.entity.TradeHistory
import com.quddaz.stock_simulator.domain.tradeHistory.repository.TradeHistoryRepository
import com.quddaz.stock_simulator.domain.tradeHistory.service.TradeHistoryService
import com.quddaz.stock_simulator.domain.user.entity.Role
import com.quddaz.stock_simulator.domain.user.entity.SocialType
import com.quddaz.stock_simulator.domain.user.entity.User
import com.quddaz.stock_simulator.domain.user.repository.UserRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertNotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import kotlin.test.Test

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class TradeHistoryServiceIntegrationTest (
    @Autowired private val tradeHistoryService: TradeHistoryService,
    @Autowired private val tradeHistoryRepository: TradeHistoryRepository,
    @Autowired private val userRepository: UserRepository,
    @Autowired private val companyRepository: CompanyRepository
) {

    private lateinit var user: User
    private lateinit var company: Company

    @BeforeEach
    fun setUp() {
        tradeHistoryRepository.deleteAll()
        userRepository.deleteAll()
        companyRepository.deleteAll()

        user = userRepository.save(
            User("user1", "user1@test.com", SocialType.GOOGLE, "id1", Role.USER)
        )

        company = companyRepository.save(
            Company(
                name = "TestCorp",
                sector = Sector.IT,
                description = "desc",
                currentPrice = 1_000L,
                totalShares = 1_000L,
                positiveRate = 1.2,
                negativeRate = 1.3
            )
        )
    }

    @Test
    fun `포트폴리오 조회`() {
        // given
        tradeHistoryRepository.save(
            TradeHistory(user = user, company= company, shareCount = 10L, price = 1_000L, record_at = LocalDateTime.now())
        )

        // when
        val portfolio = tradeHistoryService.getPortfolioByUser(user.id!!).portfolios

        // then
        assertNotNull(portfolio)
        assert(portfolio.first().companyName == company.name)
        assert(portfolio.first().shareCount == 10L)
    }

    @Test
    fun `랭킹 Top10 조회`() {
        // given
        (1..15).forEach { i ->
            val u = userRepository.save(
                User("user$i", "user$i@test.com", SocialType.GOOGLE, "id$i", Role.USER, money = 1_000_000L + i * 1000)
            )
            tradeHistoryRepository.save(
                TradeHistory(user = u, company = company, shareCount = i * 10L, price = 1_000L, record_at = LocalDateTime.now())
            )
        }

        // when
        val top10 = tradeHistoryService.getRankingTop10(defaultMoney = 10_000_000L)

        // then
        assert(top10.size == 10)
        assert(top10.first().totalAssets >= top10.last().totalAssets)
    }
}
