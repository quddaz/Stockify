package com.quddaz.stock_simulator.domain.eventHistory.entity

import com.quddaz.stock_simulator.domain.company.entity.Company
import com.quddaz.stock_simulator.domain.events.entity.Event
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime

@Entity
@Table(
    name = "event_history",
    indexes = [
        Index(name = "idx_recorded_at", columnList = "company_id ,record_at")
    ]
)
class EventHistory(
    @JoinColumn(name = "event_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    val event: Event,

    @JoinColumn(name = "company_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    val company: Company,

    @Column(name = "record_price", nullable = false)
    val recordPrice: Long, // 기록 시점 주가

    @Column(name = "change_price", nullable = false)
    val changePrice: Long, // 변동된 주가

    @Column(name = "change_rate", nullable = false)
    val changeRate: Double, // 변동률

    @CreatedDate
    @Column(name = "record_at", nullable = false)
    val record_at: LocalDateTime
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}