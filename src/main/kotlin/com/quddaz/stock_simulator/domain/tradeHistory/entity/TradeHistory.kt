package com.quddaz.stock_simulator.domain.tradeHistory.entity

import com.quddaz.stock_simulator.domain.company.entity.Company
import com.quddaz.stock_simulator.domain.user.entity.User
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime

@Entity
@Table(name = "trade_history")
class TradeHistory(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    val company: Company,

    @Column(name = "share_count", nullable = false)
    val shareCount: Long,

    @Column(name = "price", nullable = false)
    val price: Long,

    @CreatedDate
    @Column(name = "record_at", nullable = false)
    val record_at : LocalDateTime
)