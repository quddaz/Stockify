package com.quddaz.stock_simulator.domain.sectorTheme.controller

import com.quddaz.stock_simulator.domain.sectorTheme.service.SectorThemeService
import com.quddaz.stock_simulator.global.response.ResponseTemplate
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/sector-themes")
class SectorThemeController(
    private val sectorThemeService: SectorThemeService
) {

    @GetMapping("/info")
    @Operation(summary = "섹터 테마 정보 조회", description = "섹터 테마 정보를 조회합니다.")
    fun getSectorThemes(): ResponseEntity<ResponseTemplate<*>> {
        val sectorThemesInfo = sectorThemeService.getCurrentSectorThemes()
        return ResponseEntity.ok(
            ResponseTemplate.success(
                data = sectorThemesInfo
            )
        )
    }
}