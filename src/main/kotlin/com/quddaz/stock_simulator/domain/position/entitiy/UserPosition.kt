package com.quddaz.stock_simulator.domain.position.entitiy

import com.quddaz.stock_simulator.domain.company.entity.Company
import com.quddaz.stock_simulator.domain.user.entity.User
import jakarta.persistence.*


@Entity
@Table(
    name = "user_position",
    indexes = [Index(name = "idx_trade_history_user_company", columnList = "user_id, company_id")]
)
data class UserPosition(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    val company: Company,


    @Column(name = "quantity", nullable = false)
    var quantity: Long,

    @Column(name = "average_price", nullable = false)
    var averagePrice: Long
)