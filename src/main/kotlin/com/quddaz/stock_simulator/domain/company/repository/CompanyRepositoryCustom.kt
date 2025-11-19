package com.quddaz.stock_simulator.domain.company.repository

import com.quddaz.stock_simulator.domain.company.dto.CompanyStockInfoDTO
import com.quddaz.stock_simulator.domain.company.entity.Company

interface CompanyRepositoryCustom {
    fun findByNameForUpdate(companyName: String): Company
    fun findAllCompanyStockInfo(): List<CompanyStockInfoDTO>

}