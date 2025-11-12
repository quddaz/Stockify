package com.quddaz.stock_simulator.domain.sectorTheme

import com.quddaz.stock_simulator.domain.company.entity.Sector
import com.quddaz.stock_simulator.domain.sectorTheme.repository.SectorThemeRepository
import com.quddaz.stock_simulator.domain.sectorTheme.service.SectorThemeService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import kotlin.test.Test

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class SectorThemeServiceIntegrationTest(
    @Autowired private val sectorThemeService: SectorThemeService,
    @Autowired private val sectorThemeRepository: SectorThemeRepository
) {

    @BeforeEach
    fun setUp() {
        sectorThemeRepository.deleteAll()
    }

    @Test
    fun `기본 섹터 테마 저장 및 조회 테스트`() {
        // given & when
        sectorThemeService.setDefaultSectorTheme()

        // then
        val current = sectorThemeService.getCurrentSectorThemes()
        assertEquals("COMMON", current.sectorName)
        assertEquals(Sector.COMMON.positiveRate, current.positiveRate)
        assertEquals(Sector.COMMON.negativeRate, current.negativeRate)
    }

    @Test
    fun `랜덤 섹터 테마 저장 및 조회 테스트`() {
        // given & when
        sectorThemeService.setRandomSectorThemes()

        // then
        val current = sectorThemeService.getCurrentSectorThemes()
        assertNotNull(current.sectorName)
        assertTrue(current.positiveRate >= 0)
        assertTrue(current.negativeRate >= 0)
    }
}
