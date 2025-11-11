package com.quddaz.stock_simulator.domain.tradeHistory.repository

import com.quddaz.stock_simulator.domain.tradeHistory.domain.TradeHistory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface TradeHistoryRepository : JpaRepository<TradeHistory, Long>, TradeHistoryRepositoryCustom