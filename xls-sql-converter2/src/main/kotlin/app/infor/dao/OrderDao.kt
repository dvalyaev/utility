package app.infor.dao

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class OrderDao(private val jdbc: NamedParameterJdbcTemplate) {

    companion object {
        private const val SET_SCHEDULEDSHIPDATE = """
            update wmwhse1.ORDERS 
            set SCHEDULEDSHIPDATE = wmwhse1.LocalToUtc(cast(wmwhse1.UtcToLocal(SHIPMENTDATETIME) as date))
            where SCHEDULEDSHIPDATE is null
        """
    }

    fun setScheduledShipDateFromShipmentDateTime() {
        jdbc.update(SET_SCHEDULEDSHIPDATE, DaoUtils.EMPTY_MAP)
    }
}
