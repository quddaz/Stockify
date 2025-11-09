package com.quddaz.stock_simulator.domain.company.domain

import jakarta.persistence.*

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
    var currentPrice: Long
)