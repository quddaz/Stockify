package com.quddaz.stock_simulator.domain.company.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.quddaz.stock_simulator.domain.company.entity.Company
import com.quddaz.stock_simulator.domain.company.repository.CompanyRepository
import org.springframework.stereotype.Service

@Service
class CompanyService(
    private val companyRepository: CompanyRepository
) {
    private val yamlMapper = ObjectMapper(YAMLFactory()).registerKotlinModule()

    fun findByIdForUpdate(id: Long) = companyRepository.findByIdForUpdate(id)

    /** 회사 초기화 */
    fun initCompanies(yamlPath: String) {
        if (companyRepository.count() > 0) return
        val stream = javaClass.getResourceAsStream(yamlPath) ?: return
        val companies = yamlMapper.readValue(stream, Array<Company>::class.java).toList()
        companyRepository.saveAll(companies)
    }


}