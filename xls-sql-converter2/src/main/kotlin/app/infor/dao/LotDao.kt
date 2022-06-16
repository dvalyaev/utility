package app.infor.dao

import app.infor.entity.LotQty
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.math.BigDecimal

@Repository
class LotDao(private val jdbc: NamedParameterJdbcTemplate) {

    companion object {
        private const val CREATE_LOT_FROM_ATTR = """
            insert into wmwhse1.LOT(WHSEID, LOT, STORERKEY, SKU, QTY, QTYALLOCATED, QTYPICKED, QTYONHOLD, STATUS)
            select la.WHSEID, la.LOT, la.STORERKEY, la.SKU, 0, 0, 0, 0, 'OK'
            from wmwhse1.LOTATTRIBUTE la
                left join wmwhse1.LOT l on l.LOT = la.LOT
            where l.LOT is null
        """

        private const val ADD_QTY = """
            update wmwhse1.LOT set 
                QTY = QTY + :qty,
                QTYPICKED = QTYPICKED + :qtyPicked,
                QTYONHOLD = QTYONHOLD + :qtyOnHold,
                STATUS =  :status
            where LOT = :lot
            """
    }

    fun createAbsentLotsFromAttributes() {
        jdbc.update(CREATE_LOT_FROM_ATTR, MapSqlParameterSource())
    }

    fun addQty(delta: List<LotQty>): Unit = delta
        .map {
            mapOf<String, Any>(
                "lot" to it.lot,
                "qty" to it.qty,
                "qtyPicked" to it.qtyPicked,
                "qtyOnHold" to it.qtyOnHold,
                "status" to it.status.name
            )
        }
        .toTypedArray()
        .let { jdbc.batchUpdate(ADD_QTY, it) }


}