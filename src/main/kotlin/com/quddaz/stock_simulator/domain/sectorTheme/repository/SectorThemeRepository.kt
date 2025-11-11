package com.quddaz.stock_simulator.domain.sectorTheme.repository

import com.quddaz.stock_simulator.domain.sectorTheme.entity.SectorTheme
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SectorThemeRepository : JpaRepository<SectorTheme, Long>, SectorThemeRepositoryCustom {

}