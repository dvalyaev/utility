package com.example.shipping

import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post

@Controller("/wh/shipping")
class ShippingWhController {

    @Post("/ship-order")
    fun shipOrder(@Body request: ShipOrderRequest) =
        ShipOrderResponse(request.orderKeys.toSet())

    @Post("/ship-drop")
    fun shipDrop(@Body request: ShipDropRequest) =
        ShipDropResponse(setOf("order1", "order2"))
}

data class ShipOrderRequest(
    val orderKeys: List<String>
)

data class ShipOrderResponse(
    val orderKeys: Set<String>
)

data class ShipDropRequest(
    val shipId: String
)

data class ShipDropResponse(
    val orderKeys: Set<String>
)
