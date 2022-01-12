package app.infor.dao

import app.infor.entity.Balance
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class BalanceDao(private val jdbc: NamedParameterJdbcTemplate) {

    companion object {
        private const val GET_ALL = """
            select si.*, isnull(lid.OQTY, 0) QTY_PICKED
            from wmwhse1.SERIALINVENTORY si
                left join wmwhse1.LOTXIDDETAIL lid on lid.OOTHER1 = si.SERIALNUMBER
        """
    }

    fun getAll(): List<Balance> = jdbc.query(GET_ALL, MapSqlParameterSource(), ::mapRow)

    private fun mapRow(rs: ResultSet, rowNum: Int) = Balance(
        skuId = DaoUtils.mapSkuId(rs),
        lot = rs.getString("LOT"),
        loc = rs.getString("LOC"),
        id = rs.getString("ID"),
        serialNumber = rs.getString("SERIALNUMBER"),
        qty = rs.getBigDecimal("QTY"),
        qtyPicked = rs.getBigDecimal("QTY_PICKED")
    )
}