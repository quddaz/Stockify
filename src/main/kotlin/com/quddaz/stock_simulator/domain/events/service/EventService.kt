package com.quddaz.stock_simulator.domain.events.service

import com.quddaz.stock_simulator.domain.events.repository.EventRepository
import org.springframework.stereotype.Service

@Service
class EventService(
    private val eventRepository : EventRepository
) {
}