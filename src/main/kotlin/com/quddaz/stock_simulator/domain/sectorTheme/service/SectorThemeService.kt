package com.quddaz.stock_simulator.domain.sectorTheme.service

import com.quddaz.stock_simulator.domain.company.entity.Sector
import com.quddaz.stock_simulator.domain.sectorTheme.dto.SectorThemeDTO
import com.quddaz.stock_simulator.domain.sectorTheme.entity.SectorTheme
import com.quddaz.stock_simulator.domain.sectorTheme.repository.SectorThemeRepository
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class SectorThemeService(
    private val sectorThemeRepository: SectorThemeRepository,

    ) {

    @Cacheable("currentSectorThemes")
    fun getCurrentSectorThemes(): SectorThemeDTO {
        return sectorThemeRepository.getCurrentSectorTheme()
    }

    @Transactional
    @CachePut("currentSectorThemes")
    fun setRandomSectorThemes(): SectorThemeDTO {
        val sector = Sector.random()
        val sectorTheme = setSectorTheme(sector)
        return SectorThemeDTO(
            sectorName = sectorTheme.sectorName,
            positiveRate = sectorTheme.positiveRate,
            negativeRate = sectorTheme.negativeRate
        )
    }

    private fun setSectorTheme(sector: Sector): SectorTheme {
        val sectorTheme = SectorTheme(
            sectorName = sector.toString(),
            positiveRate = sector.positiveRate,
            negativeRate = sector.negativeRate
        )
        return sectorThemeRepository.save(sectorTheme)
    }

    @Transactional
    @CachePut("currentSectorThemes")
    fun setDefaultSectorTheme(): SectorThemeDTO {
        val sectorTheme = SectorTheme(
            sectorName = Sector.COMMON.toString(),
            positiveRate = Sector.COMMON.positiveRate,
            negativeRate = Sector.COMMON.negativeRate
        )

        sectorThemeRepository.save(sectorTheme)

        return SectorThemeDTO(
            sectorName = sectorTheme.sectorName,
            positiveRate = sectorTheme.positiveRate,
            negativeRate = sectorTheme.negativeRate
        )
    }
}