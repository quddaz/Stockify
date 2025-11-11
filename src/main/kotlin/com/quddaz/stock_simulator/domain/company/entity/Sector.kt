package com.quddaz.stock_simulator.domain.company.entity

enum class Sector(
    val displayName: String,
    val positiveRate: Double,
    val negativeRate: Double
) {
    IT("정보기술", 2.0, 2.0),
    BIO("바이오", 1.2, 1.2),
    FOOD("식품", 1.0, 1.0),
    AUTO("자동차", 1.5, 1.5),
    ENVIRONMENT("환경", 1.1, 1.1),
    ENERGY("에너지", 1.4, 1.4),
    RETAIL("유통", 1.0, 1.0),
    CONSTRUCTION("건설", 1.1, 1.1),
    FINANCE("금융", 0.9, 0.9),
    COMMON("일반", 1.0, 1.0);
    companion object {
        fun random(): Sector = entries.random()
    }
}
