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
import com.quddaz.stock_simulator.global.util.BaseDataInit
import jakarta.annotation.PostConstruct

/**
 * 서버 시작 시 기본 데이터를 초기화하는 클래스
 */
@BaseDataInit
class BaseDataInitializer(
    private val userRepository: UserRepository,
    private val eventRepository: EventRepository,
    private val companyRepository: CompanyRepository,
) {
    companion object {
        private const val COMPANIES_YAML = "/data/companies.yaml"
        private const val EVENTS_YAML = "/data/events.yaml"
    }
    private val objectMapper = ObjectMapper(YAMLFactory()).registerKotlinModule()

    @PostConstruct
    fun init() {
        initCompany()
        initEvents()
        initTestAdminUser()
    }

    private fun <T> loadYamlList(path: String, clazz: Class<Array<T>>): List<T> {
        val stream = javaClass.getResourceAsStream(path) ?: return emptyList()
        return objectMapper.readValue(stream, clazz).toList()
    }

    private fun initCompany() {
        if (companyRepository.count() > 0) companyRepository.deleteAll()
        val companies = loadYamlList(COMPANIES_YAML, Array<Company>::class.java)
        companyRepository.saveAll(companies)
    }

    private fun initEvents() {
        if (eventRepository.count() > 0) eventRepository.deleteAll()
        val events = loadYamlList(EVENTS_YAML, Array<Event>::class.java)
        eventRepository.saveAll(events)
    }

    private fun initTestAdminUser() {
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
