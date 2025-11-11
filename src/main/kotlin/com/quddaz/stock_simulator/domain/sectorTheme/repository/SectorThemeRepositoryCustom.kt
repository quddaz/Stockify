package com.quddaz.stock_simulator.domain.sectorTheme.repository

import com.quddaz.stock_simulator.domain.sectorTheme.domain.SectorTheme
import com.quddaz.stock_simulator.domain.sectorTheme.dto.SectorThemeDTO

interface SectorThemeRepositoryCustom {
    fun getCurrentSectorTheme(): SectorThemeDTO
}