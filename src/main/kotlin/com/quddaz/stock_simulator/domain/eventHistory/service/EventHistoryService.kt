package com.quddaz.stock_simulator.domain.eventHistory.service

import com.quddaz.stock_simulator.domain.eventHistory.repository.EventHistoryRepository
import org.springframework.stereotype.Service

@Service
class EventHistoryService(
    private val eventHistoryRepository: EventHistoryRepository
)