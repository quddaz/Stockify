package com.quddaz.stock_simulator.domain.company.repository

import com.quddaz.stock_simulator.domain.company.dto.CompanyStockInfoDTO
import com.quddaz.stock_simulator.domain.company.entity.Company
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface CompanyRepositoryCustom {
    fun findByIdForUpdate(id: Long): Company
    fun findAllCompanyStockInfo(): List<CompanyStockInfoDTO>
}