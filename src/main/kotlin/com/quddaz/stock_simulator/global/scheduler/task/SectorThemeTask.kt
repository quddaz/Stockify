package com.quddaz.stock_simulator.global.scheduler.task

import com.quddaz.stock_simulator.domain.sectorTheme.service.SectorThemeService
import com.quddaz.stock_simulator.global.scheduler.PrioritizedTask
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component


@Component
@Order(2)
class SectorThemeTask(
    private val sectorThemeService: SectorThemeService
) : PrioritizedTask {
    override fun canExecute(time: java.time.LocalDateTime): Boolean {

        return time.minute % 30 == 0
    }

    override fun execute() {
        sectorThemeService.setRandomSectorThemes()
    }

}