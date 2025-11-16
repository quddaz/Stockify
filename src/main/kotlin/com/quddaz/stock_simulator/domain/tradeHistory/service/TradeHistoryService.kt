package com.quddaz.stock_simulator.domain.tradeHistory.service

import com.quddaz.stock_simulator.domain.tradeHistory.repository.TradeHistoryRepository
import org.springframework.stereotype.Service

@Service
class TradeHistoryService(
    private val tradeHistoryRepository: TradeHistoryRepository,
)