package com.quddaz.stock_simulator.domain.user.repository

import com.quddaz.stock_simulator.domain.user.entity.QUser.user
import com.querydsl.jpa.impl.JPAQueryFactory

class UserRepositoryCustomImpl(
    private val jpaQueryFactory: JPAQueryFactory
) : UserRepositoryCustom {

    override fun setAllUserDefaultMoney(defaultMoney: Long) {
        jpaQueryFactory
            .update(user)
            .set(user.money, defaultMoney)
            .execute()
    }
}