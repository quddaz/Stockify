package com.quddaz.stock_simulator.domain.eventHistory

import com.quddaz.stock_simulator.domain.company.entity.Company
import com.quddaz.stock_simulator.domain.company.entity.Sector
import com.quddaz.stock_simulator.domain.company.repository.CompanyRepository
import com.quddaz.stock_simulator.domain.eventHistory.entity.EventHistory
import com.quddaz.stock_simulator.domain.eventHistory.repository.EventHistoryRepository
import com.quddaz.stock_simulator.domain.eventHistory.service.EventHistoryService
import com.quddaz.stock_simulator.domain.events.entity.Event
import com.quddaz.stock_simulator.domain.events.entity.EventType
import com.quddaz.stock_simulator.domain.events.repository.EventRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import kotlin.test.Test

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class EventHistoryServiceIntegrationTest(
    @Autowired private val eventHistoryService: EventHistoryService,
    @Autowired private val companyRepository: CompanyRepository,
    @Autowired private val eventRepository: EventRepository,
    @Autowired private val eventHistoryRepository: EventHistoryRepository
) {

    private lateinit var company: Company
    private lateinit var event: Event

    @BeforeEach
    fun setUp() {
        eventHistoryRepository.deleteAll()
        eventRepository.deleteAll()
        companyRepository.deleteAll()

        // 회사 및 이벤트 생성
        company = companyRepository.save(
            Company(
                name = "TestCorp",
                sector = Sector.IT,
                description = "테스트 회사",
                currentPrice = 1000L,
                totalShares = 1000L,
                positiveRate = 1.2,
                negativeRate = 1.3
            )
        )

        event = eventRepository.save(Event(
            eventType = EventType.NEGATIVE,
            description = "Test",
            weight = 10,
            impactRate = 0.1
        ))

        // EventHistory 데이터 생성
        val now = LocalDateTime.now()
        val histories = listOf(
            EventHistory(event, company, 1000L, 0L, 0.0, now.minusMinutes(2)),
            EventHistory(event, company, 1100L, 100L, 0.1, now.minusMinutes(1)),
            EventHistory(event, company, 1200L, 100L, 0.09, now)
        )
        eventHistoryRepository.saveAll(histories)
    }

    @Test
    fun `getEventHistoryByCompany 정상 조회`() {
        // when
        val result = eventHistoryService.getEventHistoryByCompany("TestCorp")

        // then
        assertThat(result).isNotNull
        assertThat(result!!.size).isEqualTo(3)
        assertThat(result[0].price).isEqualTo(1000L)
        assertThat(result[2].price).isEqualTo(1200L)
        assertThat(result).isSortedAccordingTo { o1, o2 -> o1.time.compareTo(o2.time) }
    }
}
