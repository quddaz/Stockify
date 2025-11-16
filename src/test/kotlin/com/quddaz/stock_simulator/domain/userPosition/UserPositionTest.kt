package com.quddaz.stock_simulator.domain.userPosition

import com.quddaz.stock_simulator.domain.company.entity.Company
import com.quddaz.stock_simulator.domain.company.entity.Sector
import com.quddaz.stock_simulator.domain.position.entitiy.UserPosition
import com.quddaz.stock_simulator.domain.position.exception.UserPositionDomainException
import com.quddaz.stock_simulator.domain.position.exception.errorCode.UserPositionErrorCode
import com.quddaz.stock_simulator.domain.user.entity.Role
import com.quddaz.stock_simulator.domain.user.entity.SocialType
import com.quddaz.stock_simulator.domain.user.entity.User
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertEquals

@SpringBootTest
class UserPositionTest {

    private fun createUser(): User = User(
        socialId = "socialId",
        email = "email",
        name = "name",
        socialType = SocialType.GOOGLE,
        role = Role.USER
    )

    private fun createCompany(): Company = Company(
        name = "TestCorp",
        sector = Sector.IT,
        description = "desc",
        currentPrice = 1000L,
        totalShares = 1000L,
        positiveRate = 1.2,
        negativeRate = 1.3
    )

    @Test
    fun `포지션 주식 매수`() {
        // given
        val userPosition = UserPosition(
            user = createUser(),
            company = createCompany(),
            quantity = 100L,
            averagePrice = 1000L
        )

        // when
        userPosition.buy(50L, 1200L)

        // then
        assertThat(userPosition.quantity).isEqualTo(150L)
        assertThat(userPosition.averagePrice).isEqualTo(1066L)
    }

    @Test
    fun `포지션 주식 매도`() {
        // given
        val userPosition = UserPosition(
            user = createUser(),
            company = createCompany(),
            quantity = 100L,
            averagePrice = 1000L
        )

        // when
        val sold = userPosition.sell(40L)

        // then
        assertThat(sold).isEqualTo(40L)
        assertThat(userPosition.quantity).isEqualTo(60L)
    }

    @Test
    fun `포지션 주식 매도 초과`() {
        // given
        val userPosition = UserPosition(
            user = createUser(),
            company = createCompany(),
            quantity = 100L,
            averagePrice = 1000L
        )

        // when
        val exception = assertThrows<UserPositionDomainException> {
            userPosition.sell(150L)
        }


        // then
        assertEquals(UserPositionErrorCode.INSUFFICIENT_SHARES, exception.errorCode)
    }
}