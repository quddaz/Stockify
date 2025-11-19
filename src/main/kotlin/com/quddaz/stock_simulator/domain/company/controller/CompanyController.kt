package com.quddaz.stock_simulator.domain.company.controller

import com.quddaz.stock_simulator.domain.company.dto.CompanyPageResponse
import com.quddaz.stock_simulator.domain.company.service.CompanyPriceService
import com.quddaz.stock_simulator.domain.company.service.CompanyService
import com.quddaz.stock_simulator.global.response.ResponseTemplate
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/companies")
class CompanyController(
    private val companyPriceService: CompanyPriceService,
    private val companyService: CompanyService
) {

    @GetMapping("/info")
    @Operation(summary = "회사 주식 정보 조회", description = "모든 회사의 주식 정보 캐쉬로 조회합니다.")
    fun getCompanyStockInfo(): ResponseEntity<ResponseTemplate<*>> {
        val companyStockInfoDTO = companyPriceService.getCompanyStockInfoCache()
        return ResponseEntity.ok(
            ResponseTemplate.success(
                data = companyStockInfoDTO
            )
        )
    }

    @GetMapping("/detail")
    @Operation(summary = "회사 상세 페이지 조회", description = "회사 기본 정보, 리스크 지표, 차트 데이터를 조회합니다.")
    fun getCompanyDetail(@RequestParam name: String): ResponseEntity<ResponseTemplate<CompanyPageResponse>> {
        val data = companyService.getCompanyPageResponse(name)
        return ResponseEntity.ok(ResponseTemplate.success(data))
    }
}