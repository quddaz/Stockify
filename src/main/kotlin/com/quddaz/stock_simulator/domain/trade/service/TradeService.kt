package com.quddaz.stock_simulator.domain.trade.service

import com.quddaz.stock_simulator.domain.company.entity.Company
import com.quddaz.stock_simulator.domain.company.service.CompanyService
import com.quddaz.stock_simulator.domain.position.entitiy.UserPosition
import com.quddaz.stock_simulator.domain.position.exception.UserPositionDomainException
import com.quddaz.stock_simulator.domain.position.exception.errorCode.UserPositionErrorCode
import com.quddaz.stock_simulator.domain.position.repository.UserPositionRepository
import com.quddaz.stock_simulator.domain.position.service.UserPositionService
import com.quddaz.stock_simulator.domain.trade.entity.Trade
import com.quddaz.stock_simulator.domain.trade.entity.TradeType
import com.quddaz.stock_simulator.domain.trade.repository.TradeRepository
import com.quddaz.stock_simulator.domain.user.entity.User
import com.quddaz.stock_simulator.domain.user.service.UserService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class TradeService(
    private val userService: UserService,
    private val companyService: CompanyService,
    private val positionService: UserPositionService,
    private val tradeRepository: TradeRepository
) {

    @Transactional
    fun buy(userId: Long, companyName: String, quantity: Long) {
        val user = loadUser(userId)
        val company = loadCompanyForUpdate(companyName)
        val position = loadOrCreatePosition(user, company)

        processBuy(user, company, position, quantity)
    }
    private fun processBuy(user: User, company: Company, position: UserPosition, quantity: Long) {
        val totalCost = quantity * company.currentPrice

        user.spend(totalCost)
        company.decreaseShares(quantity)

        position.buy(quantity, company.currentPrice)
        if (position.id == null) positionService.save(position)

        tradeRepository.save(
            Trade(user = user, company = company, quantity = quantity, price = company.currentPrice, type = TradeType.BUY)
        )
    }

    private fun loadUser(userId: Long) = userService.findById(userId)
    private fun loadCompanyForUpdate(name: String) = companyService.findByNameForUpdate(name)
    private fun loadOrCreatePosition(user: User, company: Company) = positionService.getOrCreatePosition(user, company)


    @Transactional
    fun sell(userId: Long, companyName: String, quantity: Long, price: Long) {
        val user = loadUser(userId)
        val company = loadCompanyForUpdate(companyName)
        val position = loadPositionOrThrow(user, company)

        processSell(user, company, position, quantity, price)
    }

    private fun loadPositionOrThrow(user: User, company: Company) =
        positionService.findByUserIdAndCompanyIdForUpdate(user.id!!, company.id!!)
            ?: throw UserPositionDomainException(UserPositionErrorCode.POSITION_NOT_FOUND)

    private fun processSell(user: User, company: Company, position: UserPosition, quantity: Long, price: Long) {
        position.sell(quantity)
        user.earn(quantity * price)
        company.increaseShares(quantity)

        if (position.quantity == 0L) positionService.delete(position)

        tradeRepository.save(Trade(user = user, company = company, quantity = quantity, price = price, type = TradeType.SELL))
    }
}
