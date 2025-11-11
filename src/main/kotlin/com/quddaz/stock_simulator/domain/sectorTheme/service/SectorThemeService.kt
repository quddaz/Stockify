package com.quddaz.stock_simulator.domain.sectorTheme.service

import com.quddaz.stock_simulator.domain.company.domain.Sector
import com.quddaz.stock_simulator.domain.sectorTheme.domain.SectorTheme
import com.quddaz.stock_simulator.domain.sectorTheme.dto.SectorThemeDTO
import com.quddaz.stock_simulator.domain.sectorTheme.repository.SectorThemeRepository

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class SectorThemeService(
    private val sectorThemeRepository: SectorThemeRepository
) {

    fun getCurrentSectorThemes(): SectorThemeDTO {
        return sectorThemeRepository.getCurrentSectorTheme()
    }

    @Transactional
    fun setSectorTheme(sector: Sector) {
        val sectorTheme = SectorTheme(
            sectorName = sector.toString(),
            positiveRate = sector.positiveRate,
            negativeRate = sector.negativeRate
        )

        sectorThemeRepository.save(sectorTheme)
    }

    @Transactional
    fun setDefaultSectorTheme() {
        val sectorTheme = SectorTheme(
            sectorName = Sector.COMMON.toString(),
            positiveRate = Sector.COMMON.positiveRate,
            negativeRate = Sector.COMMON.negativeRate
        )

        sectorThemeRepository.save(sectorTheme)
    }
}