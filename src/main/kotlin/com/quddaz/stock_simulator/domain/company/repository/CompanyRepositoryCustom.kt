package com.quddaz.stock_simulator.domain.company.repository

import com.quddaz.stock_simulator.domain.company.dto.CompanyStockInfoDTO

interface CompanyRepositoryCustom {

    fun findAllCompanyStockInfo(): List<CompanyStockInfoDTO>
}