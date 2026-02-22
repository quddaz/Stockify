package com.quddaz.stock_simulator.domain.trade.controller

import com.quddaz.stock_simulator.domain.oauth.entity.CustomOAuth2User
import com.quddaz.stock_simulator.domain.trade.dto.TradeBuyRequest
import com.quddaz.stock_simulator.domain.trade.dto.TradeSellRequest
import com.quddaz.stock_simulator.domain.trade.service.TradeProducer
import com.quddaz.stock_simulator.global.response.ResponseTemplate
import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
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
) {
    @PostMapping("/buy")
    @Operation(summary = "주식 매수 요청", description = "로그인한 유저가 주식을 매수 요청합니다.")
    fun buy(
        @AuthenticationPrincipal customOAuth2User: CustomOAuth2User, // 로그인 유저 정보
        @Valid @RequestBody request: TradeBuyRequest
    ): ResponseEntity<ResponseTemplate<*>> {
        val userId = customOAuth2User.id
        tradeProducer.sendBuy(
            userId = userId,
            companyName = request.companyName,
            quantity = request.quantity
        )
        return ResponseEntity.ok(ResponseTemplate.success("매수 요청이 큐에 전송되었습니다."))
    }

    @PostMapping("/sell")
    @Operation(summary = "주식 매도 요청", description = "로그인한 유저가 주식을 매도 요청합니다.")
    fun sell(
        @AuthenticationPrincipal customOAuth2User: CustomOAuth2User, // 로그인 유저 정보
        @Valid @RequestBody request: TradeSellRequest
    ): ResponseEntity<ResponseTemplate<*>> {
        val userId = customOAuth2User.id
        tradeProducer.sendSell(
            userId = userId,
            companyName = request.companyName,
            quantity = request.quantity
        )
        return ResponseEntity.ok(ResponseTemplate.success("매도 요청이 큐에 전송되었습니다."))
    }
}