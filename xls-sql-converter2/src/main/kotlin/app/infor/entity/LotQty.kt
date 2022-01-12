package app.infor.entity

import java.math.BigDecimal

data class LotQty(
    val lot: String,
    val qty: BigDecimal,
    val qtyPicked: BigDecimal
)