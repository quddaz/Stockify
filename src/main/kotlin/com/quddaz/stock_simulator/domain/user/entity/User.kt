package com.quddaz.stock_simulator.domain.user.entity

import com.quddaz.stock_simulator.domain.user.exception.UserDomainException
import com.quddaz.stock_simulator.domain.user.exception.errorcode.UserErrorCode
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
    var socialType: SocialType,

    @Column(name = "social_id", nullable = false)
    var socialId: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var role: Role,

    @Column(nullable = false)
    var money: Long = 10_000_000L
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    fun updateProfile(newName: String, newEmail: String) {
        this.name = newName
        this.email = newEmail
    }

    fun spend(amount: Long) {
        if(amount > money) throw UserDomainException(UserErrorCode.USER_MONEY_INSUFFICIENT)
        money -= amount
    }

    fun earn(amount: Long) {
        money += amount
    }

}