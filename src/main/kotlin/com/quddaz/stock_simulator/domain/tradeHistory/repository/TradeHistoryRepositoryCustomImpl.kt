package com.quddaz.stock_simulator.domain.tradeHistory.repository

import com.quddaz.stock_simulator.domain.company.entity.QCompany
import com.quddaz.stock_simulator.domain.company.entity.QCompany.company
import com.quddaz.stock_simulator.domain.tradeHistory.dto.PortfolioDto
import com.quddaz.stock_simulator.domain.tradeHistory.dto.UserRankingDTO
import com.quddaz.stock_simulator.domain.tradeHistory.entity.QTradeHistory
import com.quddaz.stock_simulator.domain.user.entity.QUser
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class TradeHistoryRepositoryCustomImpl(
    private val jpaQueryFactory: JPAQueryFactory,
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

    override fun findRankingTop10(defaultMoney: Long): List<UserRankingDTO> {
        val trade = QTradeHistory.tradeHistory
        val user = QUser.user
        val company = QCompany.company

        return jpaQueryFactory
            .select(
                Projections.constructor(
                    UserRankingDTO::class.java,
                    user.name,
                    user.money.add(trade.shareCount.multiply(company.currentPrice)), // 총 자산
                    user.money.add(trade.shareCount.multiply(company.currentPrice)) // 수익률
                        .subtract(defaultMoney)
                        .divide(defaultMoney.toDouble())
                )
            )
            .from(trade)
            .join(trade.user, user)
            .join(trade.company, company)
            .groupBy(user.id)
            .orderBy(user.money.add(trade.shareCount.multiply(company.currentPrice)).desc())
            .limit(10)
            .fetch()
    }
}