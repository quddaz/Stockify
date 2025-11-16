package com.quddaz.stock_simulator.domain.position.repository

import com.quddaz.stock_simulator.domain.position.entitiy.UserPosition
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserPositionRepository : JpaRepository<UserPosition, Long>, UserPositionRepositoryCustom