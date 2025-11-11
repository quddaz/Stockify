package com.quddaz.stock_simulator.domain.company.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.quddaz.stock_simulator.domain.company.entity.Company
import com.quddaz.stock_simulator.domain.company.repository.CompanyRepository
import org.springframework.stereotype.Service

@Service
class CompanyService(
    private val companyRepository: CompanyRepository,
    private val objectMapper: ObjectMapper
) {

    /** 회사 초기화 */
    fun initCompanies(yamlPath: String) {
        if (companyRepository.count() > 0) companyRepository.deleteAll()
        val stream = javaClass.getResourceAsStream(yamlPath) ?: return
        val companies = objectMapper.readValue(stream, Array<Company>::class.java).toList()
        companyRepository.saveAll(companies)
    }
}