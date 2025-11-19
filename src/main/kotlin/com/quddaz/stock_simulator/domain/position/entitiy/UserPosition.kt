package com.quddaz.stock_simulator.domain.position.entitiy

import com.quddaz.stock_simulator.domain.company.entity.Company
import com.quddaz.stock_simulator.domain.position.exception.UserPositionDomainException
import com.quddaz.stock_simulator.domain.position.exception.errorCode.UserPositionErrorCode
import com.quddaz.stock_simulator.domain.user.entity.User
import jakarta.persistence.*


@Entity
@Table(
    name = "user_position",
    indexes = [
        Index(
            name = "uk_user_position_user_company",
            columnList = "user_id, company_id",
            unique = true
        )
    ]
)
data class UserPosition(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

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
) {
    fun buy(quantityToBuy: Long, price: Long) {
        val totalQuantity = quantity + quantityToBuy
        averagePrice = ((averagePrice * quantity) + (price * quantityToBuy)) / totalQuantity
        quantity = totalQuantity
    }

    fun sell(quantityToSell: Long): Long {
        if (quantityToSell > quantity) throw UserPositionDomainException(UserPositionErrorCode.INSUFFICIENT_SHARES)
        quantity -= quantityToSell
        return quantityToSell
    }

    fun isEmpty(): Boolean = quantity == 0L
}