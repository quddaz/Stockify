package com.quddaz.stock_simulator.domain.user.repository

import com.quddaz.stock_simulator.domain.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long>, UserRepositoryCustom {
    fun findBySocialId(socialId: String): User?
    fun existsByEmail(email: String): Boolean
    fun findByEmail(email: String): User?

}