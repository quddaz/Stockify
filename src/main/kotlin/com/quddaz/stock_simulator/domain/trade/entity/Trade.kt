package com.quddaz.stock_simulator.domain.trade.entity

import com.quddaz.stock_simulator.domain.company.entity.Company
import com.quddaz.stock_simulator.domain.user.entity.User
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime

@Entity
@Table(
    name = "trade", indexes = [
        Index(name = "idx_trade_recorded_at", columnList = "record_at DESC") // 최근 거래 내역 조회 성능 향상
    ]
)
class Trade(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    val company: Company,

    @Column(name = "quantity", nullable = false)
    val quantity: Long,

    @Column(name = "price", nullable = false)
    val price: Long,

    @Enumerated(EnumType.STRING)
    @Column(name = "trade_type", nullable = false)
    val type: TradeType,

    @CreatedDate
    @Column(name = "record_at", nullable = false)
    val record_at: LocalDateTime? = LocalDateTime.now()
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}