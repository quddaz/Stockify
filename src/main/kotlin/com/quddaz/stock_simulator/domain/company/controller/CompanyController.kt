package com.quddaz.stock_simulator.domain.company.controller

import com.quddaz.stock_simulator.domain.company.service.CompanyPriceService
import com.quddaz.stock_simulator.global.response.ResponseTemplate
import com.sun.tools.javac.code.Kinds.KindSelector.VAL
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/companies")
class CompanyController(
    private val companyPriceService: CompanyPriceService
) {

    @GetMapping("/info")
    @Operation(summary = "회사 주식 정보 조회", description = "모든 회사의 주식 정보 캐쉬로 조회합니다.")
    fun getCompanyStockInfo(): ResponseEntity<ResponseTemplate<*>> {
        val companyStockInfoDTO = companyPriceService.getCompanyStockInfo()
        return ResponseEntity.ok(
            ResponseTemplate.success(
                data = companyStockInfoDTO
            )
        )
    }

}