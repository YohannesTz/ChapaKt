package com.github.yohannestz.chapakt.chapa

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChapaPayment(
    var message: String,
    var status: String,
    var data: Data? = null
)

data class Data(
    @Json(name = "checkout_url")
    var checkoutUrl: String
)