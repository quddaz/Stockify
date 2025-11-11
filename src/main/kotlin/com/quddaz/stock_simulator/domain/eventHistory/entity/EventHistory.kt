package com.quddaz.stock_simulator.domain.eventHistory.entity

import com.quddaz.stock_simulator.domain.company.entity.Company
import com.quddaz.stock_simulator.domain.events.entity.Event
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime

@Entity
@Table(name = "event_history")
class EventHistory(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "event_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    val event : Event,

    @Column(name = "company_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    val company : Company,

    @Column(name = "record_price", nullable = false)
    val recordPrice : Long,

    @Column(name = "change_price", nullable = false)
    val changePrice : Long,

    @Column(name = "change_rate", nullable = false)
    val changeRate : Double,

    @CreatedDate
    @Column(name = "record_at", nullable = false)
    val record_at : LocalDateTime
) {
}