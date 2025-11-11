package com.quddaz.stock_simulator.domain.tradeHistory.controller

import com.quddaz.stock_simulator.domain.oauth.domain.CustomOAuth2User
import com.quddaz.stock_simulator.domain.tradeHistory.service.TradeHistoryService
import com.quddaz.stock_simulator.global.exception.GlobalException
import com.quddaz.stock_simulator.global.exception.errorcode.GlobalErrorCode
import com.quddaz.stock_simulator.global.response.ResponseTemplate
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/trade-histories")
class TradeHistoryController(
    private val tradeHistoryService: TradeHistoryService
) {

    @GetMapping("/portfolio")
    @Operation(summary = "포트폴리오 조회", description = "유저의 포트폴리오를 조회합니다")
    fun getPortfolio(
        @AuthenticationPrincipal customOAuth2User: CustomOAuth2User?
    ): ResponseEntity<ResponseTemplate<*>> {
        val userId = customOAuth2User?.id
            ?: throw GlobalException(GlobalErrorCode.NOT_FOUND_USER)
        val portfolioResponse = tradeHistoryService.getPortfolioByUser(userId)
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ResponseTemplate.success(portfolioResponse))
    }
}