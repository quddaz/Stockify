package com.quddaz.stock_simulator.domain.events.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.quddaz.stock_simulator.domain.events.domain.Event
import com.quddaz.stock_simulator.domain.events.repository.EventRepository
import org.springframework.stereotype.Service

@Service
class EventService(
    private val eventRepository: EventRepository,
    private val objectMapper: ObjectMapper
) {
    /** 이벤트 초기화 */
    fun initEvents(yamlPath: String) {
        val stream = javaClass.getResourceAsStream(yamlPath) ?: return
        val events = objectMapper.readValue(stream, Array<Event>::class.java).toList()
        eventRepository.saveAll(events)
    }

}