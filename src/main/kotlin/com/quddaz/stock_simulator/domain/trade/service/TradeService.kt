package com.quddaz.stock_simulator.domain.trade.service

import com.quddaz.stock_simulator.domain.company.service.CompanyService
import com.quddaz.stock_simulator.domain.position.entitiy.UserPosition
import com.quddaz.stock_simulator.domain.position.exception.UserPositionDomainException
import com.quddaz.stock_simulator.domain.position.exception.errorCode.UserPositionErrorCode
import com.quddaz.stock_simulator.domain.position.repository.UserPositionRepository
import com.quddaz.stock_simulator.domain.trade.entity.Trade
import com.quddaz.stock_simulator.domain.trade.entity.TradeType
import com.quddaz.stock_simulator.domain.trade.repository.TradeRepository
import com.quddaz.stock_simulator.domain.user.service.UserService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class TradeService(
    private val userService: UserService,
    private val companyService: CompanyService,
    private val positionRepository: UserPositionRepository,
    private val tradeRepository: TradeRepository
) {

    @Transactional
    fun buy(userId: Long, companyId: Long, quantity: Long) {
        val user = userService.findById(userId)
        val company = companyService.findByIdForUpdate(companyId)

        val totalCost = quantity * company.currentPrice

        // 유저 돈 차감
        user.spend(totalCost)

        // 회사 주식 수 감소
        company.decreaseShares(quantity)

        // 포지션 락 + 조회
        val position = positionRepository.findByUserIdAndCompanyIdForUpdate(userId, companyId)
            ?: UserPosition(
                user = user,
                company = company,
                quantity = 0L,
                averagePrice = 0L
            )

        // 포지션 매수
        position.buy(quantity, company.currentPrice)

        // 신규 생성 시 저장
        if (position.id == null) {
            positionRepository.save(position)
        }

        val trade = Trade(
            user = user,
            company = company,
            quantity = quantity,
            price = company.currentPrice,
            type = TradeType.BUY
        )
        tradeRepository.save(trade)
    }

    @Transactional
    fun sell(userId: Long, companyId: Long, quantity: Long, price: Long) {
        val user = userService.findById(userId)
        val company = companyService.findByIdForUpdate(companyId)

        // 포지션 락 + 조회
        val position = positionRepository.findByUserIdAndCompanyIdForUpdate(userId, companyId)
            ?: throw UserPositionDomainException(UserPositionErrorCode.POSITION_NOT_FOUND)

        // 매도
        position.sell(quantity)

        // 매도 금액 획득
        user.earn(quantity * price)

        // 회사 주식 수 증가
        company.increaseShares(quantity)


        // 비어있으면 제거
        if (position.quantity == 0L) {
            positionRepository.delete(position)
        }

        val trade = Trade(
            user = user,
            company = company,
            quantity = quantity,
            price = price,
            type = TradeType.SELL
        )
        tradeRepository.save(trade)
    }
}
