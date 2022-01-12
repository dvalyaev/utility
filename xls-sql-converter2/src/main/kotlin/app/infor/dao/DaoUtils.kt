package app.infor.dao

import app.infor.entity.LotLocIdKey
import app.infor.entity.SkuId
import java.sql.ResultSet

object DaoUtils {

    val EMPTY_MAP: Map<String, Any?> = mapOf()

    fun mapSkuId(rs: ResultSet) = SkuId(
        storerKey = rs.getString("STORERKEY"),
        sku = rs.getString("SKU")
    )

    fun mapLotLocIdKey(rs: ResultSet) = LotLocIdKey(
        lot = rs.getString("LOT"),
        loc = rs.getString("LOC"),
        id = rs.getString("ID"),
    )
}
