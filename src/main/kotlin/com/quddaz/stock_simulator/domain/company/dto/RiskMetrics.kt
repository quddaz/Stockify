package com.quddaz.stock_simulator.domain.company.dto

enum class RiskLevel { LOW, MEDIUM, HIGH }
enum class Volatility { LOW, MEDIUM, HIGH }

data class RiskMetrics(
    val level: RiskLevel,
    val score: Int,
    val volatility: Volatility
)