package com.quddaz.stock_simulator.domain.company.dto

enum class RiskLevel(val minScore: Int) {
    LOW(0),
    MEDIUM(40),
    HIGH(80);

    companion object {
        fun fromScore(score: Int): RiskLevel {
            return values().last { score >= it.minScore }
        }
    }
}

enum class Volatility(val minSensitivity: Double) {
    LOW(0.0),
    MEDIUM(1.1),
    HIGH(1.5);

    companion object {
        fun fromSensitivity(sensitivity: Double): Volatility {
            return values().last { sensitivity >= it.minSensitivity }
        }
    }
}
data class RiskMetrics(
    val level: RiskLevel,
    val score: Int,
    val volatility: Volatility
)