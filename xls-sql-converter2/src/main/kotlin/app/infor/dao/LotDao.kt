package app.infor.dao

import app.infor.entity.LotQty
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.math.BigDecimal

@Repository
class LotDao(private val jdbc: NamedParameterJdbcTemplate) {

    companion object {
        private const val ADD_QTY = """
            update wmwhse1.LOT set 
                QTY = QTY + :qty,
                QTYPICKED = QTYPICKED + :qtyPicked
            where LOT = :lot
            """
    }

    fun addQty(delta: List<LotQty>): Unit = delta
        .map {
            MapSqlParameterSource()
                .addValue("lot", it.lot)
                .addValue("qty", it.qty)
                .addValue("qtyPicked", it.qtyPicked)
        }
        .toTypedArray()
        .let { jdbc.batchUpdate(ADD_QTY, it) }
}