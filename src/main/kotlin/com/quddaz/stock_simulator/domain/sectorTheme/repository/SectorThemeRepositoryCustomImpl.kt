package com.quddaz.stock_simulator.domain.sectorTheme.repository

import com.quddaz.stock_simulator.domain.sectorTheme.dto.SectorThemeDTO
import com.quddaz.stock_simulator.domain.sectorTheme.entity.QSectorTheme
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class SectorThemeRepositoryCustomImpl(
    private val jpaQueryFactory: JPAQueryFactory
) : SectorThemeRepositoryCustom {
    override fun getCurrentSectorTheme(): SectorThemeDTO {
        val sector = QSectorTheme.sectorTheme

        return jpaQueryFactory
            .select(sector)
            .from(sector)
            .orderBy(sector.updatedAt.desc())
            .limit(1)
            .fetchFirst()?.let {
                SectorThemeDTO(it.sectorName, it.positiveRate, it.negativeRate)
            } ?: SectorThemeDTO("COMMON", 1.0, 1.0)
    }
}