package com.quddaz.stock_simulator.global.scheduler

import com.quddaz.stock_simulator.domain.company.dto.CompanyStockInfoDTO
import com.quddaz.stock_simulator.domain.company.service.CompanyPriceService
import com.quddaz.stock_simulator.global.log.Loggable
import com.quddaz.stock_simulator.global.scheduler.task.TaskGroup
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Component

@Component
class StockUpdatePublisher(
    private val companyPriceService: CompanyPriceService,
    private val messagingTemplate: SimpMessagingTemplate
) : Loggable {

    /** 주식 가격 업데이트 정보를 WebSocket을 통해 구독자에게 전송 */
    fun publishStockUpdate() {
        val companyStockInfo = try {
            companyPriceService.setCompanyStockInfoCache()
        } catch (e: Exception) {
            log.error("캐시 업데이트 실패", e)
            emptyList<CompanyStockInfoDTO>()
        }

        if (companyStockInfo.isNotEmpty()) {
            messagingTemplate.convertAndSend("/topic/price-update", companyStockInfo)
        }
    }

    /** 시장 마감 알림을 WebSocket을 통해 구독자에게 전송 */
    fun publishMarketClose(mainGroup: TaskGroup) {
        if (mainGroup == TaskGroup.MARKET_CLOSE) {
            messagingTemplate.convertAndSend("/topic/market_close", "MARKET_CLOSE")
        }
    }
}
