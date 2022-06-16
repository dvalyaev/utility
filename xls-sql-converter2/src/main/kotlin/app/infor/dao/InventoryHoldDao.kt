package app.infor.dao

import app.infor.dao.DaoUtils.EMPTY_MAP
import app.infor.entity.Holds
import app.infor.entity.InventoryHold
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class InventoryHoldDao(private val jdbc: NamedParameterJdbcTemplate) {
    companion object {
        const val GET_ALL = """
            select LOT, LOC, ID, STATUS
            from wmwhse1.INVENTORYHOLD
            where HOLD = '1'
        """

        private fun mapRow(rs: ResultSet, rowNum: Int) =
            InventoryHold(
                lot = rs.getString("LOT"),
                loc = rs.getString("LOC"),
                id = rs.getString("ID"),
                status = rs.getString("STATUS"),
            )
    }

    fun getHolds(): Holds {
        val lots = mutableSetOf<String>()
        val locs = mutableSetOf<String>()
        val ids = mutableSetOf<String>()
        jdbc.query(GET_ALL, EMPTY_MAP, ::mapRow).forEach { hold ->
            hold.lot.takeIf { it.isNotBlank() } ?.also { lots += it }
            hold.loc.takeIf { it.isNotBlank() } ?.also { locs += it }
            hold.id.takeIf { it.isNotBlank() } ?.also { ids += it }
        }
        return Holds(lots, locs, ids)
    }
}