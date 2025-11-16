package com.quddaz.stock_simulator.domain.trade.controller

import com.quddaz.stock_simulator.domain.trade.service.TradeService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/trade")
class TradeController(
    private val tradeService: TradeService
)