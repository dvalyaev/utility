package app.infor.entity

import java.math.BigDecimal

data class LotLocId(
    val lotLocIdKey: LotLocIdKey,
    val skuId: SkuId,
    val qty: BigDecimal = BigDecimal.ZERO,
    val qtyPicked: BigDecimal = BigDecimal.ZERO
) {
    val lot: String get() = lotLocIdKey.lot
    val loc: String get() = lotLocIdKey.loc
    val id: String get() = lotLocIdKey.id
}

data class LotLocIdKey(val lot: String, val loc: String, val id: String)