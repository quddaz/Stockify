package com.quddaz.stock_simulator.domain.events.domain

import jakarta.persistence.*

@Entity
@Table(name = "events")
class Event(
    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false)
    val eventType: EventType,

    @Column(name = "impact_rate",nullable = false)
    val impactRate: Double,

    @Column(nullable = false)
    val description: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}