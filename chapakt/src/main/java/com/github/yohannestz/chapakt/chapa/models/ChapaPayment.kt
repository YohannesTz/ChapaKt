package com.github.yohannestz.chapakt.chapa.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class ChapaPayment(
    var message: String,
    var status: String,
    var data: Data? = null
): Serializable

data class Data(
    @Json(name = "checkout_url")
    var checkoutUrl: String
)