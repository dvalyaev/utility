package app.infor.entity

import java.math.BigDecimal

class Balance(
    val skuId: SkuId,
    val lot: String,
    val loc: String,
    val id: String,
    val serialNumber: String,
    val qty: BigDecimal,
    val qtyPicked: BigDecimal
) {
    val skuLocKey: SkuLocKey = SkuLocKey(skuId, loc)
    val lotLocIdKey: LotLocIdKey = LotLocIdKey(lot, loc, id)
}