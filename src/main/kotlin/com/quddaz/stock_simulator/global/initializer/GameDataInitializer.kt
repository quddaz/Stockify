package com.quddaz.stock_simulator.global.initializer

import com.quddaz.stock_simulator.domain.company.service.CompanyService
import com.quddaz.stock_simulator.domain.events.service.EventService
import com.quddaz.stock_simulator.domain.tradeHistory.service.TradeHistoryService
import com.quddaz.stock_simulator.domain.user.service.UserService
import com.quddaz.stock_simulator.global.log.Loggable
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class GameDataInitializer(
    private val userService: UserService,
    private val eventService: EventService,
    private val companyService: CompanyService,
    private val tradeHistoryService: TradeHistoryService,

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

        log.info("Server initialized: Admin, companies, and first round ready.")
    }

    /** 라운드 초기화 */
    fun resetRoundData() {
        //TODO: 라운드 초기화 시 필요한 다른 데이터 초기화 로직 추가(포트폴리오, 변동내역)

        initDefaultData()

        log.info("Round data reset: company reloaded and user money & dynamicData reset.")
    }

    /** 기본 데이터 초기화 */
    private fun initDefaultData() {
        companyService.initCompanies(companiesYaml)
        userService.resetAllUserMoney(initialUserMoney)
        TODO ("포트폴리오는 초기화하지 않으며 계속 데이터를 쌓아가면서 진행")
    }

    /** 동적 데이터 초기화 */
    private fun clearDynamicData() {
        tradeHistoryService.deleteAllTradeHistories()
    }


}