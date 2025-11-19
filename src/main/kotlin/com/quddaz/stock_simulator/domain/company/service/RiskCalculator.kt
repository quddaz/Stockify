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


    private fun evaluateScore(sensitivity: Double): Int {
        return (sensitivity * 50).toInt().coerceIn(0, 100)
    }

    private fun evaluateRiskLevel(score: Int): RiskLevel {
        return when {
            score >= 80 -> RiskLevel.HIGH
            score >= 40 -> RiskLevel.MEDIUM
            else -> RiskLevel.LOW
        }
    }

    private fun evaluateVolatility(sensitivity: Double): Volatility {
        return when {
            sensitivity >= 1.5 -> Volatility.HIGH
            sensitivity >= 1.1 -> Volatility.MEDIUM
            else -> Volatility.LOW
        }
    }
}