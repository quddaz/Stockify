package com.quddaz.stock_simulator.global.util.task.tasks

import com.quddaz.stock_simulator.domain.company.entity.Company
import com.quddaz.stock_simulator.domain.company.service.CompanyPriceService
import com.quddaz.stock_simulator.domain.company.service.RiskCalculator
import com.quddaz.stock_simulator.domain.eventHistory.service.EventHistoryService
import com.quddaz.stock_simulator.domain.events.service.EventService
import com.quddaz.stock_simulator.domain.sectorTheme.dto.SectorThemeDTO
import com.quddaz.stock_simulator.domain.sectorTheme.service.SectorThemeService
import com.quddaz.stock_simulator.global.log.Loggable
import com.quddaz.stock_simulator.global.util.task.PrioritizedTask
import com.quddaz.stock_simulator.global.util.task.TaskGroup
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
@Order(4)
class ChangeTask(
    private val companyPriceService: CompanyPriceService,
    private val sectorThemeService: SectorThemeService,
    private val eventHistoryService: EventHistoryService,
    private val eventService: EventService,
    private val riskCalculator: RiskCalculator
) : PrioritizedTask, Loggable {

    override val mainTask: TaskGroup = TaskGroup.CHANGE

    override val taskGroup: List<TaskGroup> = listOf(TaskGroup.CHANGE)

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
        val rate = riskCalculator.calculateRate(company, theme, baseRate)
        val oldPrice = company.currentPrice
        val event = eventService.getDefaultEvent()

        companyPriceService.updatePrice(company, rate)
        eventHistoryService.record(event, company, oldPrice, company.currentPrice, rate)
    }
}