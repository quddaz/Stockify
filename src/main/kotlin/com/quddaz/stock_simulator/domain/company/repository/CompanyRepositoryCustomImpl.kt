package com.quddaz.stock_simulator.domain.company.repository

import com.quddaz.stock_simulator.domain.company.dto.CompanyStockInfoDTO
import com.quddaz.stock_simulator.domain.company.entity.Company
import com.quddaz.stock_simulator.domain.company.entity.QCompany
import com.quddaz.stock_simulator.domain.company.exception.CompanyDomainException
import com.quddaz.stock_simulator.domain.company.exception.errorCode.CompanyErrorCode
import com.quddaz.stock_simulator.domain.eventHistory.entity.QEventHistory
import com.querydsl.core.types.Projections
import com.querydsl.jpa.JPAExpressions
import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.LockModeType
import org.springframework.stereotype.Repository

@Repository
class CompanyRepositoryCustomImpl(
    private val jpaQueryFactory: JPAQueryFactory
) : CompanyRepositoryCustom {
    override fun findByIdForUpdate(id: Long): Company {
        val company = QCompany.company

        return jpaQueryFactory
            .selectFrom(company)
            .where(company.id.eq(id))
            .setLockMode(LockModeType.PESSIMISTIC_WRITE)
            .fetchOne()
            ?: throw CompanyDomainException(CompanyErrorCode.COMPANY_NOT_FOUND)
    }

    override fun findAllCompanyStockInfo(): List<CompanyStockInfoDTO> {
        val company = QCompany.company
        val eventHistory = QEventHistory.eventHistory

        // 각 회사별 최신 이벤트 기록의 ID를 서브쿼리로 가져옵니다.
        val latestEventSub = JPAExpressions
            .select(eventHistory.id.max())
            .from(eventHistory)
            .where(eventHistory.company.eq(company))

        // 최신 이벤트 기록을 기준으로 회사의 주식 정보를 조회합니다.
        return jpaQueryFactory
            .select(
                Projections.constructor(
                    CompanyStockInfoDTO::class.java,
                    company.name,
                    eventHistory.recordPrice,
                    eventHistory.changePrice,
                    ((eventHistory.changePrice.subtract(eventHistory.recordPrice))
                        .divide(eventHistory.recordPrice)).castToNum(Double::class.java).abs(),
                    company.sector.stringValue()
                )
            )
            .from(eventHistory)
            .join(eventHistory.company, company)
            .where(eventHistory.id.`in`(latestEventSub))
            .fetch()
    }


}