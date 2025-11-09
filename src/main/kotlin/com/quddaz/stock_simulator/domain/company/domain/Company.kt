package com.quddaz.stock_simulator.domain.company.domain

import com.quddaz.stock_simulator.global.entity.BaseTimeEntity
import jakarta.persistence.*
import kotlin.math.max
import kotlin.math.roundToLong

@Entity
@Table(name = "company")
class Company(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false, unique = true)
    var name: String,

    @Column(nullable = false)
    var description: String,

    @Column(nullable = false)
    var currentPrice: Long,

    @Column(nullable = false)
    var totalShares: Long

) : BaseTimeEntity() {
    fun updatePrice(rate: Double) {
        this.currentPrice = max(0L, (this.currentPrice * (1 + rate)).roundToLong())
    }
}
