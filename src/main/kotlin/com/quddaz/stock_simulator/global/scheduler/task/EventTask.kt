package com.quddaz.stock_simulator.global.scheduler.task

import com.quddaz.stock_simulator.domain.company.entity.Company
import com.quddaz.stock_simulator.domain.company.service.CompanyPriceService
import com.quddaz.stock_simulator.domain.eventHistory.service.EventHistoryService
import com.quddaz.stock_simulator.domain.events.service.EventService
import com.quddaz.stock_simulator.domain.sectorTheme.dto.SectorThemeDTO
import com.quddaz.stock_simulator.domain.sectorTheme.service.SectorThemeService
import com.quddaz.stock_simulator.global.log.Loggable
import com.quddaz.stock_simulator.global.scheduler.PrioritizedTask
import com.quddaz.stock_simulator.global.scheduler.TaskGroup
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Component
@Order(3)
class EventTask(
    private val companyPriceService: CompanyPriceService,
    private val sectorThemeService: SectorThemeService,
    private val eventHistoryService: EventHistoryService,
    private val eventService: EventService
) : PrioritizedTask, Loggable {

    override val mainTask: TaskGroup = TaskGroup.EVENT

    override val taskGroup: List<TaskGroup> = listOf(TaskGroup.MARKET_CLOSE, TaskGroup.SECTOR_THEME, TaskGroup.EVENT)
    override fun canExecute(time: java.time.LocalDateTime): Boolean {
        return time.minute % 15 == 0
    }

    override fun execute() {
        val theme = sectorThemeService.getCurrentSectorThemes()
        companyPriceService.getAllCompanies().forEach { processCompany(it, theme) }
        log.info("EventTask start executing")
    }

    private fun processCompany(company: Company, theme: SectorThemeDTO) {
        val event = eventService.getWeightedRandomEvent()
        val rate = companyPriceService.calculateRate(company, theme, event.impactRate)
        val oldPrice = company.currentPrice
        companyPriceService.updatePrice(company, rate)
        eventHistoryService.record(event, company, oldPrice, company.currentPrice, rate)
    }

}