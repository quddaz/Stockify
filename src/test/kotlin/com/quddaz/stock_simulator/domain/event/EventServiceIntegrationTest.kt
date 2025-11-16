package com.quddaz.stock_simulator.domain.event

import com.quddaz.stock_simulator.domain.events.repository.EventRepository
import com.quddaz.stock_simulator.domain.events.service.EventService
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import kotlin.test.assertTrue

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class EventServiceIntegrationTest(
    @Autowired private val eventService: EventService,
    @Autowired private val eventRepository: EventRepository
) {

    @Test
    fun `이벤트 초기화`() {
        // given
        eventService.initEvents("/data/test_events.yaml")

        // when
        val events = eventRepository.findAll()

        // then
        assertTrue(events.isNotEmpty(), "이벤트 초기화 실패")
    }

    @Test
    fun `이벤트 초기화 및 랜덤 메소드 `() {
        // given
        eventService.initEvents("/data/test_events.yaml")

        // when
        val event = eventService.getWeightedRandomEvent()

        // then
        assertNotNull(event)
        assertTrue(eventRepository.findAll().contains(event))
    }
}
