package app.infor.dao

import app.infor.entity.LotLocId
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class LotLocIdDao(private val jdbc: NamedParameterJdbcTemplate) {

    companion object {
        private const val INSERT = """
            insert into wmwhse1.LOTXLOCXID (STORERKEY, SKU, LOT, LOC, ID, QTY, QTYPICKED) 
            values(:storerKey, :sku, :lot, :loc, :id, :qty, :qtyPicked)
            """
    }

    fun insert(lotLocIds: Collection<LotLocId>): Unit = lotLocIds
        .map {
            MapSqlParameterSource()
                .addValue("storerKey", it.skuId.storerKey)
                .addValue("sku", it.skuId.sku)
                .addValue("lot", it.lotLocIdKey.lot)
                .addValue("loc", it.lotLocIdKey.loc)
                .addValue("id", it.lotLocIdKey.id)
                .addValue("qty", it.qty)
                .addValue("qtyPicked", it.qtyPicked)
        }
        .toTypedArray()
        .let { jdbc.batchUpdate(INSERT, it) }
}