package com.quddaz.stock_simulator.domain.company

import com.quddaz.stock_simulator.domain.company.entity.Company
import com.quddaz.stock_simulator.domain.company.entity.Sector
import com.quddaz.stock_simulator.domain.company.service.RiskCalculator
import com.quddaz.stock_simulator.domain.sectorTheme.dto.SectorThemeDTO
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class RiskCalculatorTest {

    private val calculator = RiskCalculator()
    lateinit var company: Company
    lateinit var theme: SectorThemeDTO

    @BeforeEach
    fun setUp() {
        company = Company(
            name = "TestCorp",
            sector = Sector.IT,
            description = "desc",
            currentPrice = 100_000L,
            totalShares = 1_000L,
            positiveRate = 0.8,
            negativeRate = 0.2
        )
        theme = SectorThemeDTO(
            sectorName = "Tech Boom",
            positiveRate = 0.5,
            negativeRate = 0.3
        )
    }
    @Test
    fun `계산 베이스가 양수일때`() {
        // given & when
        val rate = calculator.calculateRate(company, theme, 1.0)

        // then
        assertEquals(0.4, rate) // 1.0 * 0.8 * 0.5
    }

    @Test
    fun `계산 베이스가 음수일때`() {
        // given & when
        val rate = calculator.calculateRate(company, theme, -1.0)

        // then
        assertEquals(-0.06, rate) // -1.0 * 0.2 * 0.3
    }

    @Test
    fun `반응성 판별`() {
        // given & when & then
        assertEquals(100, calculator.evaluateScore(3.0)) // 3*50 > 100
        assertEquals(0, calculator.evaluateScore(-1.0))  // -50 < 0
    }
}
