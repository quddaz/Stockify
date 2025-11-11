package com.quddaz.stock_simulator.domain.events.entity

import jakarta.persistence.*

@Entity
@Table(name = "events")
class Event(
    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false)
    val eventType: EventType,

    @Column(name = "impact_rate", nullable = false)
    val impactRate: Double,

    @Column(name = "description",nullable = false)
    val description: String,

    @Column(name = "weight", nullable = false)
    val weight: Long
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}