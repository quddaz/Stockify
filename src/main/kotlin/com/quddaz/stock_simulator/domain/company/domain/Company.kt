package com.quddaz.stock_simulator.domain.company.domain

import com.quddaz.stock_simulator.global.entity.BaseTimeEntity
import jakarta.persistence.*
import kotlin.math.max
import kotlin.math.roundToLong

@Entity
@Table(name = "company")
class Company(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long? = null,

    @Column(name = "name", nullable = false, unique = true)
    val name: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "sector", nullable = false)
    val sector: Sector,

    @Column(name = "description", nullable = false)
    val description: String,

    @Column(name = "current_price", nullable = false)
    var currentPrice: Long,

    @Column(name = "total_shares", nullable = false)
    var totalShares: Long,

    @Column(name = "positive_late", nullable = false)
    val positiveRate : Double,

    @Column(name = "negative_late", nullable = false)
    val negativeRate : Double
) : BaseTimeEntity() {
    fun updatePrice(rate: Double) {
        this.currentPrice = max(0L, (this.currentPrice * (1 + rate)).roundToLong())
    }
}
