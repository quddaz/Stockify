package com.quddaz.stock_simulator.global.scheduler.task

import com.quddaz.stock_simulator.domain.company.entity.Company
import com.quddaz.stock_simulator.domain.company.service.CompanyPriceService
import com.quddaz.stock_simulator.domain.eventHistory.service.EventHistoryService
import com.quddaz.stock_simulator.domain.events.service.EventService
import com.quddaz.stock_simulator.domain.sectorTheme.dto.SectorThemeDTO
import com.quddaz.stock_simulator.domain.sectorTheme.service.SectorThemeService
import com.quddaz.stock_simulator.global.scheduler.PrioritizedTask
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Component
@Order(3)
class EventTask(
    private val companyPriceService: CompanyPriceService,
    private val sectorThemeService: SectorThemeService,
    private val eventHistoryService: EventHistoryService,
    private val eventService: EventService
) : PrioritizedTask {
    override fun canExecute(time: java.time.LocalDateTime): Boolean {
        return time.minute % 15 == 0
    }

    override fun execute() {
        val theme = sectorThemeService.getCurrentSectorThemes()
        companyPriceService.getAllCompanies().forEach { processCompany(it, theme) }
    }

    private fun processCompany(company: Company, theme: SectorThemeDTO) {
        val event = eventService.getWeightedRandomEvent()
        val rate = companyPriceService.calculateRate(company, theme, event.impactRate)
        val oldPrice = company.currentPrice
        companyPriceService.updatePrice(company, rate)
        eventHistoryService.record(event, company, oldPrice, company.currentPrice, rate)
    }

}