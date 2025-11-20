package com.quddaz.stock_simulator.domain.trade

import com.quddaz.stock_simulator.domain.company.entity.Company
import com.quddaz.stock_simulator.domain.company.entity.Sector
import com.quddaz.stock_simulator.domain.company.repository.CompanyRepository
import com.quddaz.stock_simulator.domain.position.repository.UserPositionRepository
import com.quddaz.stock_simulator.domain.trade.entity.TradeType
import com.quddaz.stock_simulator.domain.trade.repository.TradeRepository
import com.quddaz.stock_simulator.domain.trade.service.TradeService
import com.quddaz.stock_simulator.domain.user.entity.Role
import com.quddaz.stock_simulator.domain.user.entity.SocialType
import com.quddaz.stock_simulator.domain.user.entity.User
import com.quddaz.stock_simulator.domain.user.repository.UserRepository
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired


@ActiveProfiles("test")
@SpringBootTest
@Transactional
class TradeServiceIntegrationTest(
    @Autowired private val tradeService: TradeService,
    @Autowired private val userRepository: UserRepository,
    @Autowired private val companyRepository: CompanyRepository,
    @Autowired private val positionRepository: UserPositionRepository,
    @Autowired private val tradeRepository: TradeRepository
) {

    private lateinit var user: User
    private lateinit var company: Company

    @BeforeEach
    fun setUp() {
        // DB 초기화
        tradeRepository.deleteAll()
        positionRepository.deleteAll()
        companyRepository.deleteAll()
        userRepository.deleteAll()

        // 도메인 객체만 생성
        user = createUser()
        company = createCompany()
    }

    private fun createUser(): User = User(
        socialId = "socialId",
        email = "email@test.com",
        name = "테스터",
        socialType = SocialType.GOOGLE,
        role = Role.USER,
        money = 100_000L // 초기 자금
    )

    private fun createCompany(): Company = Company(
        name = "TestCorp",
        sector = Sector.IT,
        description = "테스트 회사",
        currentPrice = 1_000L,
        totalShares = 1_000L,
        positiveRate = 1.2,
        negativeRate = 1.3
    )

    @Test
    fun `주식 매수 시 사용자 잔액, 회사 주식 수, 포지션, 거래 기록이 정상 업데이트`() {
        // given: Repository를 통해 저장
        val savedUser = userRepository.save(user)
        val savedCompany = companyRepository.save(company)

        // when
        tradeService.buy(savedUser.id!!, savedCompany.name, 10L)

        // then
        val updatedUser = userRepository.findById(savedUser.id!!).get()
        assertThat(updatedUser.money).isEqualTo(100_000L - 10 * 1_000L)

        val updatedCompany = companyRepository.findById(savedCompany.id!!).get()
        assertThat(updatedCompany.totalShares).isEqualTo(1_000L - 10L)

        val position = positionRepository.findByUserIdAndCompanyIdForUpdate(savedUser.id!!, savedCompany.id!!)
        assertThat(position).isNotNull
        assertThat(position!!.quantity).isEqualTo(10L)
        assertThat(position.averagePrice).isEqualTo(1_000L)

        val trade = tradeRepository.findAll().firstOrNull()
        assertThat(trade).isNotNull
        assertThat(trade!!.type).isEqualTo(TradeType.BUY)
        assertThat(trade.quantity).isEqualTo(10L)
        assertThat(trade.price).isEqualTo(1_000L)
    }

    @Test
    fun `주식 매도 시 사용자 잔액, 회사 주식 수, 포지션이 정상 업데이트`() {
        // given
        val savedUser = userRepository.save(user)
        val savedCompany = companyRepository.save(company)
        tradeService.buy(savedUser.id!!, savedCompany.name, 10L)

        // when
        tradeService.sell(savedUser.id!!, savedCompany.name, 5L, 1_100L)

        // then
        val updatedUser = userRepository.findById(savedUser.id!!).get()
        assertThat(updatedUser.money).isEqualTo(100_000L - 10 * 1_000L + 5 * 1_100L)

        val updatedCompany = companyRepository.findById(savedCompany.id!!).get()
        assertThat(updatedCompany.totalShares).isEqualTo(1_000L - 10L + 5L)

        val position = positionRepository.findByUserIdAndCompanyIdForUpdate(savedUser.id!!, savedCompany.id!!)
        assertThat(position).isNotNull
        assertThat(position!!.quantity).isEqualTo(5L)
    }

    @Test
    fun `포지션 전량 매도 시 포지션이 삭제`() {
        // given
        val savedUser = userRepository.save(user)
        val savedCompany = companyRepository.save(company)
        tradeService.buy(savedUser.id!!, savedCompany.name, 10L)

        // when
        tradeService.sell(savedUser.id!!, savedCompany.name, 10L, 1_100L)

        // then
        val position = positionRepository.findByUserIdAndCompanyIdForUpdate(savedUser.id!!, savedCompany.id!!)
        assertThat(position).isNull()
    }
}
