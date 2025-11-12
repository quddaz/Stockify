package com.quddaz.stock_simulator.global.scheduler.task

import com.quddaz.stock_simulator.domain.company.entity.Company
import com.quddaz.stock_simulator.domain.company.service.CompanyPriceService
import com.quddaz.stock_simulator.domain.eventHistory.service.EventHistoryService
import com.quddaz.stock_simulator.domain.sectorTheme.dto.SectorThemeDTO
import com.quddaz.stock_simulator.domain.sectorTheme.service.SectorThemeService
import com.quddaz.stock_simulator.global.log.Loggable
import com.quddaz.stock_simulator.global.scheduler.PrioritizedTask
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
@Order(4)
class ChangeTask(
    private val companyPriceService: CompanyPriceService,
    private val sectorThemeService: SectorThemeService,
    private val eventHistoryService: EventHistoryService,
) : PrioritizedTask, Loggable {
    override fun canExecute(time: LocalDateTime): Boolean {
        return time.minute % 5 == 0
    }

    override fun execute() {
        val theme = sectorThemeService.getCurrentSectorThemes()
        companyPriceService.getAllCompanies().forEach { processCompany(it, theme) }

        log.info("ChangeTask start executing")
    }

    private fun processCompany(company: Company, theme: SectorThemeDTO) {
        val baseRate = (-5..5).random().toDouble() * 0.01 // -5% ~ +5%
        val rate = companyPriceService.calculateRate(company, theme, baseRate)
        val oldPrice = company.currentPrice

        companyPriceService.updatePrice(company, rate)
        eventHistoryService.record(null , company, oldPrice, company.currentPrice, rate)
    }
}