package com.quddaz.stock_simulator.domain.eventHistory.repository

import com.quddaz.stock_simulator.domain.company.entity.QCompany.company
import com.quddaz.stock_simulator.domain.eventHistory.dto.StockChartDataResponse
import com.quddaz.stock_simulator.domain.eventHistory.entity.QEventHistory.eventHistory
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class EventHistoryRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory
) : EventHistoryRepositoryCustom {

    override fun findChartDataByCompanyName(companyName: String): List<StockChartDataResponse>? {
        return queryFactory
            .select(
                Projections.constructor(
                    StockChartDataResponse::class.java,
                    eventHistory.record_at,
                    eventHistory.recordPrice
                )
            )
            .from(eventHistory)
            .join(eventHistory.company, company)
            .where(company.name.eq(companyName))
            .orderBy(eventHistory.record_at.asc())
            .limit(100)
            .fetch()
    }
}