package com.quddaz.stock_simulator.domain.tradeHistory.repository

import com.quddaz.stock_simulator.domain.company.entity.QCompany.company
import com.quddaz.stock_simulator.domain.tradeHistory.dto.PortfolioDto
import com.quddaz.stock_simulator.domain.tradeHistory.entity.QTradeHistory
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class TradeHistoryRepositoryCustomImpl(
    private val jpaQueryFactory: JPAQueryFactory
) : TradeHistoryRepositoryCustom {

    override fun findPortfolioByUser(userId: Long): List<PortfolioDto> {
        val trade = QTradeHistory.tradeHistory
        return jpaQueryFactory
            .select(
                Projections.constructor(
                    PortfolioDto::class.java,
                    company.id,
                    company.name,
                    trade.shareCount.sum(), // 총 보유량
                    trade.shareCount.multiply(trade.price).sum().divide(trade.shareCount.sum()), // 평균 매입가
                    company.currentPrice, // 현재 가격
                    company.currentPrice.multiply(trade.shareCount.sum()).subtract(
                        trade.shareCount.multiply(trade.price).sum()
                    ) // 미실현 손익
                )
            )
            .from(trade)
            .join(trade.company, company)
            .where(trade.user.id.eq(userId))
            .groupBy(trade.company.id)
            .fetch()
    }
}