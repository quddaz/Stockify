package com.quddaz.stock_simulator.domain.position.repository

import com.quddaz.stock_simulator.domain.company.entity.QCompany
import com.quddaz.stock_simulator.domain.position.dto.PortfolioDTO
import com.quddaz.stock_simulator.domain.position.dto.PortfolioResponse
import com.quddaz.stock_simulator.domain.position.dto.UserRankingDTO
import com.quddaz.stock_simulator.domain.position.dto.UserRankingResponse
import com.quddaz.stock_simulator.domain.position.entitiy.QUserPosition
import com.quddaz.stock_simulator.domain.user.entity.QUser
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class UserPositionRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory
) : UserPositionRepositoryCustom {
    override fun findPortfolioByUser(userId: Long): PortfolioResponse {
        val position = QUserPosition.userPosition
        val company = QCompany.company

        val result = queryFactory
            .select(
                Projections.constructor(
                    PortfolioDTO::class.java,
                    company.id,
                    company.name,
                    position.quantity,
                    position.averagePrice,
                    company.currentPrice,
                    company.currentPrice.multiply(position.quantity),                            // 평가금액
                    company.currentPrice.subtract(position.averagePrice).multiply(position.quantity)   // 미실현 손익
                )
            )
            .from(position)
            .join(position.company, company)
            .where(position.user.id.eq(userId))
            .fetch()

        return PortfolioResponse(result)
    }

    override fun findRankings(defaultMoney: Long): UserRankingResponse{
        val u = QUser.user
        val p = QUserPosition.userPosition
        val c = QCompany.company

        val stockValue = p.quantity.multiply(c.currentPrice)

        val result = queryFactory
            .select(
                Projections.constructor(
                    UserRankingDTO::class.java,
                    u.name,
                    u.money.add(stockValue.sum()),                    // 총 자산
                    u.money.add(stockValue.sum())
                        .subtract(defaultMoney)
                        .divide(defaultMoney.toDouble())            // 수익률
                )
            )
            .from(u)
            .leftJoin(p).on(p.user.eq(u))
            .leftJoin(p.company, c)
            .groupBy(u.id)  // u.id만 그룹핑해도 충분
            .orderBy(u.money.add(stockValue.sum()).desc())
            .limit(10)
            .fetch()

        return UserRankingResponse(result)
    }

}