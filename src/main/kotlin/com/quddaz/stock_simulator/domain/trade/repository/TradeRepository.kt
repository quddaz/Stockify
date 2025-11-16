package com.quddaz.stock_simulator.domain.trade.repository

import com.quddaz.stock_simulator.domain.trade.entity.Trade
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface TradeRepository : JpaRepository<Trade, Long>