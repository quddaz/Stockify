package com.quddaz.stock_simulator.domain.eventHistory.service

import com.quddaz.stock_simulator.domain.company.entity.Company
import com.quddaz.stock_simulator.domain.eventHistory.entity.EventHistory
import com.quddaz.stock_simulator.domain.eventHistory.repository.EventHistoryRepository
import com.quddaz.stock_simulator.domain.events.entity.Event
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional(readOnly = true)
class EventHistoryService(
    private val eventHistoryRepository: EventHistoryRepository
) {
    @Transactional
    fun record(event: Event?, company: Company, oldPrice: Long, newPrice: Long, changeRate: Double) {
        val changePrice = newPrice - oldPrice

        val history = EventHistory(
            event = event ?: Event.getDefaultEvent(),
            company = company,
            recordPrice = oldPrice,
            changePrice = changePrice,
            changeRate = changeRate,
            record_at = LocalDateTime.now()
        )

        eventHistoryRepository.save(history)
    }
}