package com.github.yohannestz.chapakt.chapa

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class ChapaResponse(
    var message: String,
    var status: String,
    var data: PaymentData?
): Serializable

@Json(name = "data")
data class PaymentData (
    @Json(name = "first_name")
    var firstName: String,
    @Json(name = "last_name")
    var lastName: String,
    var email: String,
    var currency: String,
    var amount: Double,
    var charge: Double,
    var mode: String,
    var method: String,
    var type: String,
    var reference: String,
    @Json(name = "tx_ref")
    var txRef: String,
    var customization: Customization,
    var meta: String?,
    @Json(name = "created_at")
    var createdAt: String,
    @Json(name = "updated_at")
    var updatedAt: String
)

data class Customization (
    var title: String?,
    var description: String?,
    var logo: String?
)
