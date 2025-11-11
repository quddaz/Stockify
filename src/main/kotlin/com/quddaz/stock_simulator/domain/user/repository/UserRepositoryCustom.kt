package com.quddaz.stock_simulator.domain.user.repository

import org.springframework.stereotype.Repository

@Repository
interface UserRepositoryCustom {

    fun setAllUserDefaultMoney(defaultMoney: Long)
}