package app.infor.dao

import app.infor.entity.LotLocId
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class LotLocIdDao(private val jdbc: NamedParameterJdbcTemplate) {

    companion object {
        private const val INSERT = """
            insert into wmwhse1.LOTXLOCXID (STORERKEY, SKU, LOT, LOC, ID, QTY, QTYPICKED, STATUS) 
            values(:storerKey, :sku, :lot, :loc, :id, :qty, :qtyPicked, :status)
            """
    }

    fun insert(lotLocIds: Collection<LotLocId>): Unit = lotLocIds
        .map {
            mapOf<String, Any>(
                "storerKey" to it.skuId.storerKey,
                "sku" to it.skuId.sku,
                "lot" to it.lotLocIdKey.lot,
                "loc" to it.lotLocIdKey.loc,
                "id" to it.lotLocIdKey.id,
                "qty" to it.qty,
                "qtyPicked" to it.qtyPicked,
                "status" to it.status.name
            )
        }
        .toTypedArray()
        .let { jdbc.batchUpdate(INSERT, it) }
}