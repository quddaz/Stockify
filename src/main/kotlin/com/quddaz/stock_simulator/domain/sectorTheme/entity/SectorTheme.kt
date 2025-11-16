package com.quddaz.stock_simulator.domain.sectorTheme.entity

import jakarta.persistence.*
import java.time.LocalDateTime


@Entity
@Table(name = "sector_theme")
data class SectorTheme(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(name = "sector_name", nullable = false)
    val sectorName: String,
    @Column(name = "positive_rate", nullable = false)
    val positiveRate: Double,
    @Column(name = "negative_rate", nullable = false)
    val negativeRate: Double,
    @Column(name = "updated_at")
    val updatedAt: LocalDateTime? = null
)