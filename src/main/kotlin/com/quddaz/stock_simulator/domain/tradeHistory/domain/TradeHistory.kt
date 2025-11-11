package com.quddaz.stock_simulator.domain.tradeHistory.domain

import com.quddaz.stock_simulator.domain.company.domain.Company
import com.quddaz.stock_simulator.domain.user.entity.User
import jakarta.persistence.*

@Entity
@Table(name = "trade_history")
class TradeHistory(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    var company: Company,

    @Column(name = "share_count", nullable = false)
    var shareCount: Long,

    @Column(name = "price", nullable = false)
    var price: Long
)