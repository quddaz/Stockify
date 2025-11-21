package com.quddaz.stock_simulator.domain.trade

import com.quddaz.stock_simulator.domain.company.entity.Company
import com.quddaz.stock_simulator.domain.company.entity.Sector
import com.quddaz.stock_simulator.domain.company.exception.CompanyDomainException
import com.quddaz.stock_simulator.domain.company.service.CompanyService
import com.quddaz.stock_simulator.domain.position.entitiy.UserPosition
import com.quddaz.stock_simulator.domain.position.exception.UserPositionDomainException
import com.quddaz.stock_simulator.domain.position.service.UserPositionService
import com.quddaz.stock_simulator.domain.trade.repository.TradeRepository
import com.quddaz.stock_simulator.domain.trade.service.TradeService
import com.quddaz.stock_simulator.domain.user.entity.Role
import com.quddaz.stock_simulator.domain.user.entity.SocialType
import com.quddaz.stock_simulator.domain.user.entity.User
import com.quddaz.stock_simulator.domain.user.exception.UserDomainException
import com.quddaz.stock_simulator.domain.user.service.UserService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class)
class TradeServiceTest {

    @Mock
    lateinit var userService: UserService

    @Mock
    lateinit var companyService: CompanyService

    @Mock
    lateinit var positionService: UserPositionService

    @Mock
    lateinit var tradeRepository: TradeRepository

    @InjectMocks
    lateinit var tradeService: TradeService

    private lateinit var user: User
    private lateinit var company: Company
    private lateinit var position: UserPosition

    @BeforeEach
    fun setUp() {
        // 테스트용 유저 초기화
        user = User(
            socialId = "socialId",
            email = "email@test.com",
            name = "테스터",
            socialType = SocialType.GOOGLE,
            role = Role.USER,
            money = 100_000L
        ).apply { id = 1L }

        // 테스트용 회사 초기화
        company = Company(
            name = "TestCorp",
            sector = Sector.IT,
            description = "테스트 회사",
            currentPrice = 1_000L,
            totalShares = 1_000L,
            positiveRate = 1.2,
            negativeRate = 1.3
        ).apply { id = 1L }

        // 테스트용 포지션 초기화
        position = UserPosition(
            user = user,
            company = company,
            quantity = 0L,
            averagePrice = 0L
        )
    }

    @Test
    fun `buy 메서드 정상 동작`() {
        // given
        whenever(userService.findById(user.id!!)).thenReturn(user)
        whenever(companyService.findByNameForUpdate(company.name)).thenReturn(company)
        whenever(positionService.getOrCreatePosition(user, company)).thenReturn(position)
        whenever(positionService.save(any())).thenAnswer { it.arguments[0] }

        // when
        tradeService.buy(user.id!!, company.name, 10L)

        // then
        assertThat(user.money).isEqualTo(100_000L - 10 * 1_000L)
        assertThat(company.totalShares).isEqualTo(1_000L - 10L)
        verify(positionService).save(any())
        verify(tradeRepository).save(any())
    }

    @Test
    fun `buy 시 유저 돈보다 많은 돈을 사용`() {
        // given
        whenever(userService.findById(user.id!!)).thenReturn(user)
        whenever(companyService.findByNameForUpdate(company.name)).thenReturn(company)
        whenever(positionService.getOrCreatePosition(user, company)).thenReturn(position)

        // when & then
        assertThrows<UserDomainException> {
            tradeService.buy(user.id!!, company.name, 100_000L)
        }
    }

    @Test
    fun `buy 시 회사 보유 주식 수보다 많이 사용`() {
        // given
        val smallCompany = Company(
            name = "TestCorp",
            sector = Sector.IT,
            description = "테스트 회사",
            currentPrice = 1_000L,
            totalShares = 10L,
            positiveRate = 1.2,
            negativeRate = 1.3
        ).apply { id = 1L }

        whenever(userService.findById(user.id!!)).thenReturn(user)
        whenever(companyService.findByNameForUpdate(smallCompany.name)).thenReturn(smallCompany)
        whenever(positionService.getOrCreatePosition(user, smallCompany)).thenReturn(position)

        // when & then
        assertThrows<CompanyDomainException> {
            tradeService.buy(user.id!!, smallCompany.name, 11L)
        }
    }

    @Test
    fun `sell 메서드 정상 동작`() {
        // given
        position.buy(10L, 1_000L)
        whenever(userService.findById(user.id!!)).thenReturn(user)
        whenever(companyService.findByNameForUpdate(company.name)).thenReturn(company)
        whenever(positionService.findByUserIdAndCompanyIdForUpdate(user.id!!, company.id!!)).thenReturn(position)

        // when
        tradeService.sell(user.id!!, company.name, 5L, 1_100L)

        // then
        assertThat(user.money).isEqualTo(100_000L - 10 * 1_000L + 5 * 1_100L)
        assertThat(company.totalShares).isEqualTo(1_000L - 10 + 5)
        assertThat(position.quantity).isEqualTo(5L)
        verify(tradeRepository).save(any())
    }

    @Test
    fun `sell 시 원래보다 많은 수량을 매도`() {
        // given
        position.buy(10L, 1_000L)
        whenever(userService.findById(user.id!!)).thenReturn(user)
        whenever(companyService.findByNameForUpdate(company.name)).thenReturn(company)
        whenever(positionService.findByUserIdAndCompanyIdForUpdate(user.id!!, company.id!!)).thenReturn(position)

        // when & then
        assertThrows<UserPositionDomainException> {
            tradeService.sell(user.id!!, company.name, 100L, 1_100L)
        }
    }

    @Test
    fun `포지션 전량 매도 시 포지션 삭제`() {
        // given
        position.buy(10L, 1_000L)
        whenever(userService.findById(user.id!!)).thenReturn(user)
        whenever(companyService.findByNameForUpdate(company.name)).thenReturn(company)
        whenever(positionService.findByUserIdAndCompanyIdForUpdate(user.id!!, company.id!!)).thenReturn(position)

        // when
        tradeService.sell(user.id!!, company.name, 10L, 1_100L)

        // then
        assertThat(position.quantity).isEqualTo(0L)
        verify(positionService).delete(position)
        verify(tradeRepository).save(any())
    }
}
