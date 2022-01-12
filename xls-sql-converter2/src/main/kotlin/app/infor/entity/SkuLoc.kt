package app.infor.entity

import java.math.BigDecimal

data class SkuLoc(
    val skuLocKey: SkuLocKey,
    val locType: String,
    val qty: BigDecimal = BigDecimal.ZERO,
    val qtyPicked: BigDecimal = BigDecimal.ZERO
) {
    val skuId: SkuId get() = skuLocKey.skuId
    val loc: String get() = skuLocKey.loc
}

data class SkuLocKey(val skuId: SkuId, val loc: String)