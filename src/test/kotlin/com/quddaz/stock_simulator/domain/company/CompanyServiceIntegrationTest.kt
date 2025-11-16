package com.quddaz.stock_simulator.domain.company

import com.quddaz.stock_simulator.domain.company.repository.CompanyRepository
import com.quddaz.stock_simulator.domain.company.service.CompanyPriceService
import com.quddaz.stock_simulator.domain.company.service.CompanyService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import kotlin.math.roundToLong

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class CompanyServiceIntegrationTest(
    @Autowired private val companyService: CompanyService,
    @Autowired private val companyRepository: CompanyRepository,
    @Autowired private val companyPriceService: CompanyPriceService
) {

    @Test
    fun `회사 초기화 및 주가 업데이트`() {
        // given
        companyService.initCompanies("/data/test_companies.yaml")

        val companies = companyRepository.findAll()
        assert(companies.isNotEmpty()) { "회사 초기화 실패" }

        // when
        val company = companies.first()
        val oldPrice = company.currentPrice
        val rate = 0.1
        companyPriceService.updatePrice(company, rate)

        // then
        val expectedPrice = (oldPrice * (1 + rate)).roundToLong()
        assertEquals(expectedPrice, company.currentPrice)
    }
}
