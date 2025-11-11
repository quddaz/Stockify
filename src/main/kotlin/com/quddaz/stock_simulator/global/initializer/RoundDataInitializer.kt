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
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class RoundDataInitializer(
    private val userRepository: UserRepository,
    private val eventRepository: EventRepository,
    private val companyRepository: CompanyRepository,
) {
    companion object {
        private const val COMPANIES_YAML = "/data/companies.yaml"
        private const val EVENTS_YAML = "/data/events.yaml"
    }

    private val objectMapper = ObjectMapper(YAMLFactory()).registerKotlinModule()

    /** 서버 시작 시 1회 실행 */
    @EventListener(ApplicationReadyEvent::class)
    fun onApplicationReady() {
        initializeBaseData()
    }

    fun resetRoundData() {
        clearDynamicData()
        initializeRoundDefaults()
    }

    private fun initializeBaseData() {
        initCompanies()
        initEvents()
        initAdminUser()
    }

    private fun initializeRoundDefaults() {
        initEvents()
        // TODO: 기타 라운드 초기화 데이터

    }

    private fun clearDynamicData() {
        eventRepository.deleteAll()
        // TODO: 기타 라운드 초기화 데이터 삭제
    }

    private fun <T> loadYamlList(path: String, clazz: Class<Array<T>>): List<T> {
        val stream = javaClass.getResourceAsStream(path) ?: return emptyList()
        return objectMapper.readValue(stream, clazz).toList()
    }

    private fun initCompanies() {
        if (companyRepository.count() > 0) companyRepository.deleteAll()
        val companies = loadYamlList(COMPANIES_YAML, Array<Company>::class.java)
        companyRepository.saveAll(companies)
    }

    private fun initEvents() {
        if (eventRepository.count() > 0) eventRepository.deleteAll()
        val events = loadYamlList(EVENTS_YAML, Array<Event>::class.java)
        eventRepository.saveAll(events)
    }

    private fun initAdminUser() {
        if (!userRepository.existsByEmail("admin@example.com")) {
            userRepository.save(
                User(
                    name = "admin",
                    email = "admin@example.com",
                    socialType = SocialType.GOOGLE,
                    socialId = "admin-social-id",
                    money = 10_000_000L,
                    role = Role.ADMIN
                )
            )
        }
    }
}

