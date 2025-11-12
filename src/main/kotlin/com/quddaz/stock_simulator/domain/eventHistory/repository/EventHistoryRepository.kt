package com.quddaz.stock_simulator.domain.eventHistory.repository

import com.quddaz.stock_simulator.domain.eventHistory.entity.EventHistory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EventHistoryRepository : JpaRepository<EventHistory, Long>