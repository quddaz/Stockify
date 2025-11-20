package com.quddaz.stock_simulator.domain.company.service


import com.quddaz.stock_simulator.domain.company.dto.RiskLevel
import com.quddaz.stock_simulator.domain.company.dto.RiskMetrics
import com.quddaz.stock_simulator.domain.company.dto.Volatility
import com.quddaz.stock_simulator.domain.company.entity.Company
import com.quddaz.stock_simulator.domain.sectorTheme.dto.SectorThemeDTO
import org.springframework.stereotype.Component
import kotlin.math.abs
import kotlin.math.max

@Component
class RiskCalculator {

    companion object {
        private const val SCORE_MULTIPLIER = 50
        private const val MAX_SCORE = 100
        private const val MIN_SCORE = 0
    }

    fun analyze(company: Company, theme: SectorThemeDTO): RiskMetrics {
        val maxSensitivity = calculateMaxSensitivity(company, theme)
        val score = evaluateScore(maxSensitivity)
        val level = evaluateRiskLevel(score)
        val volatility = evaluateVolatility(maxSensitivity)

        return RiskMetrics(level, score, volatility)
    }

    private fun calculateMaxSensitivity(company: Company, theme: SectorThemeDTO): Double {
        val upSensitivity = calculateRate(company, theme, 1.0)
        val downSensitivity = abs(calculateRate(company, theme, -1.0))
        return max(upSensitivity, downSensitivity)
    }

    fun calculateRate(company: Company, theme: SectorThemeDTO, baseRate: Double): Double {
        return if (baseRate >= 0) {
            baseRate * (company.positiveRate * theme.positiveRate)
        } else {
            baseRate * (company.negativeRate * theme.negativeRate)
        }
    }

    fun evaluateScore(sensitivity: Double): Int {
        return (sensitivity * SCORE_MULTIPLIER).toInt().coerceIn(MIN_SCORE, MAX_SCORE)
    }

    private fun evaluateRiskLevel(score: Int): RiskLevel {
        return RiskLevel.fromScore(score)
    }

    private fun evaluateVolatility(sensitivity: Double): Volatility {
        return Volatility.fromSensitivity(sensitivity)
    }
}
