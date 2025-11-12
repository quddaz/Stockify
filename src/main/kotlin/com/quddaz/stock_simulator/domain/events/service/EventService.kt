package com.quddaz.stock_simulator.domain.events.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.quddaz.stock_simulator.domain.events.entity.Event
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


    /** 가중치 기반 무작위 이벤트 선택 */
    fun getWeightedRandomEvent(): Event {
        val events = eventRepository.findAll()

        val totalWeight = events.sumOf { it.weight }
        val rand = (0 until totalWeight).random()  // 0부터 totalWeight-1 사이의 무작위 값 생성
        var accumulated = 0L

        for (event in events) {
            accumulated += event.weight
            if (rand < accumulated) return event
        }
        return Event.getDefaultEvent()
    }

    /** LongRange에서 무작위 Long 값 생성 */
    private fun LongRange.random(): Long {
        return start + kotlin.random.Random.nextLong(endInclusive - start + 1)
    }

}