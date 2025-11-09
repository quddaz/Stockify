package com.quddaz.stock_simulator.domain.company.repository

import com.quddaz.stock_simulator.domain.company.domain.Company
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CompanyRepository : JpaRepository<Company, Long> {
}