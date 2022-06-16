package app.infor.service

import app.infor.dao.LotDao
import org.springframework.stereotype.Service

@Service
class FixDbService(
    private val lotDao: LotDao,
    private val balanceService: BalanceService,
    private val orderService: OrderService,
) {
    fun fixDb() {
        lotDao.createAbsentLotsFromAttributes()
        balanceService.fixBalances()
        orderService.setScheduledShipDateFromShipmentDateTime()
    }
}
