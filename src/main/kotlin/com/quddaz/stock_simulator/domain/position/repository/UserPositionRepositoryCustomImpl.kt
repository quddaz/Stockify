package com.quddaz.stock_simulator.domain.position.repository

import com.quddaz.stock_simulator.domain.company.entity.QCompany
import com.quddaz.stock_simulator.domain.position.dto.PortfolioDTO
import com.quddaz.stock_simulator.domain.position.dto.PortfolioResponse
import com.quddaz.stock_simulator.domain.position.dto.UserRankingDTO
import com.quddaz.stock_simulator.domain.position.dto.UserRankingResponse
import com.quddaz.stock_simulator.domain.position.entitiy.QUserPosition
import com.quddaz.stock_simulator.domain.position.entitiy.UserPosition
import com.quddaz.stock_simulator.domain.position.exception.UserPositionDomainException
import com.quddaz.stock_simulator.domain.user.dto.UserDto
import com.quddaz.stock_simulator.domain.user.entity.QUser
import com.quddaz.stock_simulator.domain.user.exception.errorcode.UserErrorCode
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.LockModeType
import org.springframework.stereotype.Repository

@Repository
class UserPositionRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory
) : UserPositionRepositoryCustom {
    override fun findPortfolioByUser(userId: Long): PortfolioResponse {
        val p = QUserPosition.userPosition
        val c = QCompany.company
        val u = QUser.user
        val user = queryFactory
            .select(
                Projections.constructor(
                    UserDto::class.java,
                    u.name,
                    u.money
                )
            )
            .from(u)
            .where(u.id.eq(userId))
            .fetchOne() ?: throw UserPositionDomainException(UserErrorCode.USER_NOT_FOUND)

        val result = queryFactory
            .select(
                Projections.constructor(
                    PortfolioDTO::class.java,
                    c.id,
                    c.name,
                    p.quantity,
                    p.averagePrice,
                    c.currentPrice,
                    c.currentPrice.multiply(p.quantity),                            // 평가금액
                    c.currentPrice.subtract(p.averagePrice).multiply(p.quantity)   // 미실현 손익
                )
            )
            .from(p)
            .join(p.company, c)
            .where(p.user.id.eq(userId))
            .fetch()

        return PortfolioResponse(result, user)
    }

    override fun findRankings(defaultMoney: Long): UserRankingResponse {
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

    override fun findByUserIdAndCompanyIdForUpdate(userId: Long, companyId: Long): UserPosition? {
        val p = QUserPosition.userPosition
        return queryFactory
            .selectFrom(p)
            .where(p.user.id.eq(userId).and(p.company.id.eq(companyId)))
            .setLockMode(LockModeType.PESSIMISTIC_WRITE)
            .fetchOne()
    }
}