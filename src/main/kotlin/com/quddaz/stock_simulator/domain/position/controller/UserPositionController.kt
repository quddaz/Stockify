package com.quddaz.stock_simulator.domain.position.controller

import com.quddaz.stock_simulator.domain.oauth.entity.CustomOAuth2User
import com.quddaz.stock_simulator.domain.position.service.UserPositionService
import com.quddaz.stock_simulator.global.response.ResponseTemplate
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user-positions")
class UserPositionController(
    private val userPositionService: UserPositionService
) {

    @GetMapping("/portfolio")
    @Operation(summary = "포트폴리오 조회", description = "유저의 포지션(포트폴리오)을 조회합니다")
    fun getPortfolio(
        @AuthenticationPrincipal customOAuth2User: CustomOAuth2User
    ): ResponseEntity<ResponseTemplate<*>> {
        val userId = customOAuth2User.id
        val portfolioResponse = userPositionService.getPortfolioByUser(userId)
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ResponseTemplate.success(portfolioResponse))
    }

    @GetMapping("/ranking")
    @Operation(summary = "랭킹 Top 10 조회", description = "랭킹 Top 10을 캐시로 조회합니다")
    fun getRankingTop10(): ResponseEntity<ResponseTemplate<*>> {
        val rankingTop10 = userPositionService.getRankingTop10FromCache()
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ResponseTemplate.success(rankingTop10))
    }
}