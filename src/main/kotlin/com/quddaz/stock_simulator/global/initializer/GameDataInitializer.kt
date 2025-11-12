package com.quddaz.stock_simulator.global.initializer

import com.quddaz.stock_simulator.domain.company.service.CompanyService
import com.quddaz.stock_simulator.domain.events.service.EventService
import com.quddaz.stock_simulator.domain.sectorTheme.service.SectorThemeService
import com.quddaz.stock_simulator.domain.user.service.UserService
import com.quddaz.stock_simulator.global.log.Loggable
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class GameDataInitializer(
    private val userService: UserService,
    private val eventService: EventService,
    private val companyService: CompanyService,
    private val sectorThemeService: SectorThemeService,

    @Value("\${data.companies-yaml-path}")
    private val companiesYaml: String,
    @Value("\${data.events-yaml-path}")
    private val eventsYaml: String,
    @Value("\${data.initial-user-money}")
    private val initialUserMoney: Long,
    @Value("\${data.admin-email}")
    private val adminEmail: String

) : Loggable, CommandLineRunner {

    /** 서버 시작 후 초기화 작업 수행 */
    override fun run(vararg args: String?) {
        userService.initAdminUser(adminEmail, initialUserMoney)
        eventService.initEvents(eventsYaml)
        companyService.initCompanies(companiesYaml)
        sectorThemeService.setDefaultSectorTheme()

        log.info("Server initialized: Admin, companies, and first round ready.")
    }
}