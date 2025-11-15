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

}
