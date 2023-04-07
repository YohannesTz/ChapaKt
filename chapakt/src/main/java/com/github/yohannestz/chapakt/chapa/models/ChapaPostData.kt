package com.github.yohannestz.chapakt.chapa.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChapaPostData(
    var amount: Double = 0.01,
    var currency: String,
    var email: String,
    @Json(name = "first_name")
    var firstName: String,
    @Json(name = "last_name")
    var lastName: String,
    @Json(name = "tx_ref")
    var txRef: String,
    @Json(name = "return_url")
    var returnUrl: String,
    @Json(name = "customization[title]")
    var customizationTitle: String? = null,
    @Json(name = "customization[description]")
    var customizationDescription: String? = null,
    var customizationLogo: String? = null,
    var subAccountId: String? = null,
)