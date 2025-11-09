package com.quddaz.stock_simulator.domain.company.domain

import jakarta.persistence.*

@Entity
@Table(name = "company")
class Company(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    var name: String,

    var description: String? = null,

    @Column(nullable = false)
    var currentPrice: Long
)