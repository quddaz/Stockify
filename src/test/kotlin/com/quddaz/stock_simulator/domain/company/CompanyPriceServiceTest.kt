package com.quddaz.stock_simulator.domain.company

import com.quddaz.stock_simulator.domain.company.entity.Company
import com.quddaz.stock_simulator.domain.company.entity.Sector
import com.quddaz.stock_simulator.domain.company.repository.CompanyRepository
import com.quddaz.stock_simulator.domain.company.service.CompanyPriceService
import com.quddaz.stock_simulator.domain.sectorTheme.dto.SectorThemeDTO
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever
import kotlin.math.roundToLong
import kotlin.test.Test


@ExtendWith(MockitoExtension::class)
class CompanyPriceServiceTest {

    @Mock
    lateinit var companyRepository: CompanyRepository

    lateinit var service: CompanyPriceService

    @BeforeEach
    fun setUp() {
        service = CompanyPriceService(companyRepository)
    }

    @Test
    fun `변동률 양수 계산`() {
        // given
        val company = Company(
            id = 1L,
            name = "TestCorp",
            sector = Sector.IT,
            description = "desc",
            currentPrice = 1000L,
            totalShares = 1000L,
            positiveRate = 1.2,
            negativeRate = 1.3
        )

        val theme = SectorThemeDTO(sectorName = "IT", positiveRate = 1.5, negativeRate = 1.1)
        val baseRate = 0.05 // 양수

        // when
        val rate = service.calculateRate(company, theme, baseRate)

        // then
        assertEquals(baseRate * company.positiveRate * theme.positiveRate, rate)
    }

    @Test
    fun `변동률 음수 계산`() {
        // given
        val company = Company(
            id = 1L,
            name = "TestCorp",
            sector = Sector.IT,
            description = "desc",
            currentPrice = 1000L,
            totalShares = 1000L,
            positiveRate = 1.2,
            negativeRate = 1.3
        )

        val theme = SectorThemeDTO(sectorName = "IT", positiveRate = 1.5, negativeRate = 1.1)
        val baseRate = -0.05 // 음수

        // when
        val rate = service.calculateRate(company, theme, baseRate)

        // then
        assertEquals(baseRate * company.negativeRate * theme.negativeRate, rate)
    }

    @Test
    fun `가격 업데이트 검증`() {
        // given
        val company = Company(
            id = 1L,
            name = "TestCorp",
            sector = Sector.IT,
            description = "desc",
            currentPrice = 1000L,
            totalShares = 1000L,
            positiveRate = 1.2,
            negativeRate = 1.3
        )

        val rate = 0.1

        val expectedPrice = (company.currentPrice * (1 + rate)).roundToLong()

        // when
        whenever(companyRepository.save(company)).thenReturn(company)
        service.updatePrice(company, rate)

        // then
        assertEquals(expectedPrice, company.currentPrice)
    }
}
