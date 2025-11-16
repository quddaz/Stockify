package com.quddaz.stock_simulator.domain.tradeHistory.controller

import com.quddaz.stock_simulator.domain.tradeHistory.service.TradeHistoryService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/trade-histories")
class TradeHistoryController(
    private val tradeHistoryService: TradeHistoryService
)