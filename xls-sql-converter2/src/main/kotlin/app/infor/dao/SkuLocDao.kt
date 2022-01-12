package app.infor.dao

import app.infor.entity.LotLocId
import app.infor.entity.SkuLoc
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class SkuLocDao(private val jdbc: NamedParameterJdbcTemplate) {

    companion object {
        private const val INSERT = """
            insert into wmwhse1.SKUXLOC (STORERKEY, SKU, LOC, LOCATIONTYPE, QTY, QTYPICKED) 
            values(:storerKey, :sku, :loc, :locType, :qty, :qtyPicked)
            """
    }

    fun insert(skuLocs: Collection<SkuLoc>): Unit = skuLocs
        .map {
            MapSqlParameterSource()
                .addValue("storerKey", it.skuId.storerKey)
                .addValue("sku", it.skuId.sku)
                .addValue("loc", it.loc)
                .addValue("locType", it.locType)
                .addValue("qty", it.qty)
                .addValue("qtyPicked", it.qtyPicked)
        }
        .toTypedArray()
        .let { jdbc.batchUpdate(INSERT, it) }
}