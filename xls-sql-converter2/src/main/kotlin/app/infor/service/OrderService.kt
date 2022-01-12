package app.infor.service

import app.infor.dao.OrderDao
import org.springframework.stereotype.Service

@Service
class OrderService(private val orderDao: OrderDao) {

    fun setScheduledShipDateFromShipmentDateTime() {
        orderDao.setScheduledShipDateFromShipmentDateTime()
    }
}
