package com.quddaz.stock_simulator.domain.trade.controller

import com.quddaz.stock_simulator.domain.oauth.entity.CustomOAuth2User
import com.quddaz.stock_simulator.domain.trade.dto.TradeBuyRequest
import com.quddaz.stock_simulator.domain.trade.dto.TradeEvent
import com.quddaz.stock_simulator.domain.trade.dto.TradeSellRequest
import com.quddaz.stock_simulator.domain.trade.service.TradeProducer
import com.quddaz.stock_simulator.domain.trade.service.TradeService
import com.quddaz.stock_simulator.global.response.ResponseTemplate
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/trade")
class TradeController(
    private val tradeProducer: TradeProducer
){
    @PostMapping("/buy")
    fun buy(
        @AuthenticationPrincipal customOAuth2User: CustomOAuth2User, // 로그인 유저 정보
        @RequestBody request: TradeBuyRequest
    ): ResponseEntity<ResponseTemplate<*>> {
        val userId = customOAuth2User.id
        tradeProducer.send(
            TradeEvent.BuyEvent(
                userId = userId,
                stockId = request.companyId,
                quantity = request.quantity,
                price = request.price
            )
        )
        return ResponseEntity.ok(ResponseTemplate.success("매수 요청이 큐에 전송되었습니다."))
    }

    @PostMapping("/sell")
    fun sell(
        @AuthenticationPrincipal customOAuth2User: CustomOAuth2User, // 로그인 유저 정보
        @RequestBody request: TradeSellRequest
    ): ResponseEntity<ResponseTemplate<*>> {
        val userId = customOAuth2User.id
        tradeProducer.send(
            TradeEvent.SellEvent(
                userId = userId,
                stockId = request.companyId,
                quantity = request.quantity,
                price = request.price
            )
        )
        return ResponseEntity.ok(ResponseTemplate.success("매도 요청이 큐에 전송되었습니다."))
    }
}