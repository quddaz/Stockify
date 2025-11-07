package com.quddaz.stock_simulator.domain.user.entity

import jakarta.persistence.*
@Entity
@Table(name = "users")
class User(
    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var email: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "social_type", nullable = false, length = 20)
    val socialType: SocialType,

    @Column(name = "social_id", nullable = false)
    val socialId: String,

    @Column(nullable = false)
    var money: Long = 0L
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    fun updateProfile(newName: String, newEmail: String) {
        this.name = newName
        this.email = newEmail
    }

    fun buyStock(amount: Long) {
        if (this.money - amount < 0) throw IllegalArgumentException("금액은 0보다 작을 수 없습니다.")
        this.money -= amount
    }
}