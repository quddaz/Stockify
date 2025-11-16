package com.quddaz.stock_simulator.domain.company

import com.quddaz.stock_simulator.domain.company.entity.Company
import com.quddaz.stock_simulator.domain.company.entity.Sector
import com.quddaz.stock_simulator.domain.company.exception.CompanySharesException
import com.quddaz.stock_simulator.domain.company.exception.errorCode.CompanyErrorCode
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertEquals

@SpringBootTest
class CompanyTest {
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
    fun `회사 주식 가격 테스트`() {
        // given
        val company = createCompany()
        // when
        company.updatePrice(0.1)

        // then
        assertThat(company.currentPrice > 1000L)
    }

    @Test
    fun `회사 주식 매수`() {
        // given
        val company = createCompany()

        // when
        company.decreaseShares(1000)

        // then
        assertThat(company.totalShares).isEqualTo(0L)
    }

    @Test
    fun `회사 주식 매수 초과`() {
        // given
        val company = createCompany()

        // when
        val exception = assertThrows<CompanySharesException> {
            company.decreaseShares(1500)
        }

        // then
        assertEquals(CompanyErrorCode.NOT_ENOUGH_SHARES, exception.errorCode)
    }

    @Test
    fun `회사 주식 매도`() {
        // given
        val company = createCompany()
        // when
        company.increaseShares(500)

        // then
        assertThat(company.totalShares).isEqualTo(1500L)
    }

}