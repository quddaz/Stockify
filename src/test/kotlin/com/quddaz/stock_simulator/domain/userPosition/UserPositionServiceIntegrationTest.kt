package com.quddaz.stock_simulator.domain.userPosition

import com.quddaz.stock_simulator.domain.company.entity.Company
import com.quddaz.stock_simulator.domain.company.entity.Sector
import com.quddaz.stock_simulator.domain.company.repository.CompanyRepository
import com.quddaz.stock_simulator.domain.position.entitiy.UserPosition
import com.quddaz.stock_simulator.domain.position.repository.UserPositionRepository
import com.quddaz.stock_simulator.domain.position.service.UserPositionService
import com.quddaz.stock_simulator.domain.user.entity.Role
import com.quddaz.stock_simulator.domain.user.entity.SocialType
import com.quddaz.stock_simulator.domain.user.entity.User
import com.quddaz.stock_simulator.domain.user.repository.UserRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.assertNotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import kotlin.test.Test


@ActiveProfiles("test")
@SpringBootTest
@Transactional
class UserPositionServiceIntegrationTest(
    @Autowired private val userPositionService: UserPositionService,
    @Autowired private val userPositionRepository: UserPositionRepository,
    @Autowired private val userRepository: UserRepository,
    @Autowired private val companyRepository: CompanyRepository
) {
    fun createCompany(): Company = Company(
        name = "TestCorp",
        sector = Sector.IT,
        description = "desc",
        currentPrice = 60_000L,
        totalShares = 1_000L,
        positiveRate = 1.2,
        negativeRate = 1.3
    )

    @Test
    fun `포트폴리오 조회`() {
        //given
        val user =
            userRepository.save(User("test", "test@test.com", SocialType.GOOGLE, "id", Role.USER, money = 10_000_000L))
        val company = companyRepository.save(
            createCompany()
        )
        userPositionRepository.save(
            UserPosition(
                user = user,
                company = company,
                quantity = 10L,
                averagePrice = 50_000L
            )
        )

        // when
        val portfolio = userPositionService.getPortfolioByUser(user.id ?: 1L)

        // then
        assertNotNull(portfolio)
        assertEquals(company.name, portfolio.positions.first().companyName)
        assertEquals(10L, portfolio.positions.first().quantity)
    }

    @Test
    fun `랭킹 Top10 조회`() {
        // given
        val company = companyRepository.save(
            createCompany()
        )
        (1..15).forEach { i ->
            val u = userRepository.save(
                User("user$i", "user$i@test.com", SocialType.GOOGLE, "id$i", Role.USER, money = 1_000_000L + i * 1000)
            )
            userPositionRepository.save(
                UserPosition(
                    user = u,
                    company = company,
                    quantity = 10L * i,
                    averagePrice = 50_000L
                )
            )
        }

        // when
        val top10 = userPositionService.updateRankingTop10()

        // then
        assertEquals(10, top10.size)
        assertTrue(top10.first().totalAssets >= top10.last().totalAssets)
    }
}