package com.quddaz.stock_simulator.domain.company.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.quddaz.stock_simulator.domain.company.dto.CompanyPageResponse
import com.quddaz.stock_simulator.domain.company.entity.Company
import com.quddaz.stock_simulator.domain.company.exception.CompanyDomainException
import com.quddaz.stock_simulator.domain.company.exception.errorCode.CompanyErrorCode
import com.quddaz.stock_simulator.domain.company.repository.CompanyRepository
import com.quddaz.stock_simulator.domain.eventHistory.service.EventHistoryService
import com.quddaz.stock_simulator.domain.sectorTheme.service.SectorThemeService
import org.springframework.stereotype.Service

@Service
class CompanyService(
    private val companyRepository: CompanyRepository,
    private val eventHistoryService: EventHistoryService,
    private val riskCalculator: RiskCalculator,
    private val sectorThemeService: SectorThemeService
) {
    private val yamlMapper = ObjectMapper(YAMLFactory()).registerKotlinModule()

    fun getCompanyPageResponse(name: String): CompanyPageResponse {
        val company =
            companyRepository.findByName(name)
        val chartData = eventHistoryService.getEventHistoryByCompany(name) ?: emptyList()
        val riskMetrics = riskCalculator.analyze(company, sectorThemeService.getCurrentSectorThemes())

        return CompanyPageResponse(
            companyName = company.name,
            sector = company.sector.toString(),
            description = company.description,
            currentPrice = company.currentPrice,
            totalShares = company.totalShares,
            riskMetrics = riskMetrics,
            chart = chartData
        )
    }

    fun findByNameForUpdate(companyName: String) = companyRepository.findByNameForUpdate(companyName)

    /** 회사 초기화 */
    fun initCompanies(yamlPath: String) {
        if (companyRepository.count() > 0) return
        val stream = javaClass.getResourceAsStream(yamlPath) ?: return
        val companies = yamlMapper.readValue(stream, Array<Company>::class.java).toList()
        companyRepository.saveAll(companies)
    }


}