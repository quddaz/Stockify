package com.quddaz.stock_simulator.domain.events.repository

import com.quddaz.stock_simulator.domain.events.entity.Event
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EventRepository : JpaRepository<Event, Long>