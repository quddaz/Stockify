package com.quddaz.stock_simulator.domain.company.service

import com.quddaz.stock_simulator.domain.company.dto.CompanyStockInfoDTO
import com.quddaz.stock_simulator.domain.company.entity.Company
import com.quddaz.stock_simulator.domain.company.repository.CompanyRepository
import com.quddaz.stock_simulator.domain.sectorTheme.dto.SectorThemeDTO
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class CompanyPriceService(
    private val companyRepository: CompanyRepository
) {
    fun getAllCompanies(): List<Company> {
        return companyRepository.findAll()
    }

    fun calculateRate(company: Company, theme: SectorThemeDTO, baseRate: Double): Double {
        if (baseRate >= 0) {
            val positiveRate = company.positiveRate * theme.positiveRate
            return baseRate * positiveRate
        }
        val negativeRate = company.negativeRate * theme.negativeRate
        return baseRate * negativeRate
    }

    @Transactional
    fun updatePrice(company: Company, rate: Double) {
        company.updatePrice(rate)
        companyRepository.save(company)
    }

    @Cacheable("companyStockInfo")
    fun getCompanyStockInfoCache(): List<CompanyStockInfoDTO> {
        return companyRepository.findAllCompanyStockInfo()
    }

    @CachePut("companyStockInfo")
    fun setCompanyStockInfoCache(): List<CompanyStockInfoDTO> {
        return companyRepository.findAllCompanyStockInfo()
    }
}