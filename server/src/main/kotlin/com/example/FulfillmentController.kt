package com.example

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post

@Controller("fulfillment")
class FulfillmentController {

    @Post("getStocks")
    fun getStocks(): String = FileUtils.readResource("/responses/fulfillment/getStocks1.json")

}