package com.quddaz.stock_simulator.domain.company.service

import com.quddaz.stock_simulator.domain.company.repository.CompanyRepository
import org.springframework.stereotype.Service

@Service
class CompanyService(
    private val companyRepository: CompanyRepository
) {
}