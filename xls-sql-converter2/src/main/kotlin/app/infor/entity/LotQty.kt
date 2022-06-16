package app.infor.entity

import app.infor.enum.HoldStatus
import java.math.BigDecimal

data class LotQty(
    val lot: String,
    val qty: BigDecimal,
    val qtyPicked: BigDecimal,
    val qtyOnHold: BigDecimal,
    val status: HoldStatus,
)