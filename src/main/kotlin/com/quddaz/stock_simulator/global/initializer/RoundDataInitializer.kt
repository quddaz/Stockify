package com.quddaz.stock_simulator.global.initializer

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.quddaz.stock_simulator.domain.company.domain.Company
import com.quddaz.stock_simulator.domain.company.repository.CompanyRepository
import com.quddaz.stock_simulator.domain.events.domain.Event
import com.quddaz.stock_simulator.domain.events.repository.EventRepository
import com.quddaz.stock_simulator.domain.user.entity.Role
import com.quddaz.stock_simulator.domain.user.entity.SocialType
import com.quddaz.stock_simulator.domain.user.entity.User
import com.quddaz.stock_simulator.domain.user.repository.UserRepository
import com.quddaz.stock_simulator.global.log.Loggable
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class RoundDataInitializer(
    private val userRepository: UserRepository,
    private val eventRepository: EventRepository,
    private val companyRepository: CompanyRepository,
) : Loggable {

    companion object {
        private const val COMPANIES_YAML = "/data/companies.yaml"
        private const val EVENTS_YAML = "/data/events.yaml"
        private const val INITIAL_USER_MONEY = 10_000_000L
        private const val ADMIN_EMAIL = "admin@example.com"
    }

    private val objectMapper: ObjectMapper = ObjectMapper(YAMLFactory()).registerKotlinModule()

    /** 서버 시작 시 1회 실행 */
    @EventListener(ApplicationReadyEvent::class)
    fun onApplicationReady() {
        initAdminUser()
        initCompanies()
        resetRoundData() // 첫 라운드 초기화
        log.info("Server started: Admin user and companies initialized, first round ready.")
    }

    /** 라운드 시작 시 호출 */
    fun resetRoundData() {
        clearDynamicData()
        initEvents()
        log.info("Round data reset: Events reloaded and user money reset.")
    }

    /** 라운드 단위로 초기화해야 하는 데이터 삭제 */
    private fun clearDynamicData() {
        userRepository.setAllUserDefaultMoney(INITIAL_USER_MONEY)
        // TODO : 주식 보유 내역 초기화 등 추가 작업 필요
    }

    /** 관리자 유저 초기화 (서버 시작 시 1회) */
    private fun initAdminUser() {
        if (!userRepository.existsByEmail(ADMIN_EMAIL)) {
            userRepository.save(
                User(
                    name = "admin",
                    email = ADMIN_EMAIL,
                    socialType = SocialType.GOOGLE,
                    socialId = "admin-social-id",
                    money = INITIAL_USER_MONEY,
                    role = Role.ADMIN
                )
            )
            log.info("Admin user created: $ADMIN_EMAIL")
        }
    }

    /** 회사 초기화 (서버 시작 시 1회) */
    private fun initCompanies() {
        if (companyRepository.count() > 0) companyRepository.deleteAll()
        val companies = loadYamlList(COMPANIES_YAML, Array<Company>::class.java)
        companyRepository.saveAll(companies)
        log.info("${companies.size} companies initialized from YAML.")
    }

    /** 이벤트 초기화 (라운드 시작 시) */
    private fun initEvents() {
        val events = loadYamlList(EVENTS_YAML, Array<Event>::class.java)
        eventRepository.saveAll(events)
        log.info("${events.size} events loaded from YAML.")
    }

    /** YAML 파일 로딩 유틸 */
    private fun <T> loadYamlList(path: String, clazz: Class<Array<T>>): List<T> {
        val stream = javaClass.getResourceAsStream(path) ?: return emptyList()
        return objectMapper.readValue(stream, clazz).toList()
    }
}
