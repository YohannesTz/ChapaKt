package com.github.yohannestz.chapakt.chapa.models

import com.squareup.moshi.Json

@Json
data class Transfer(
    @Json(name = "account_name")
    var accountName: String,
    @Json(name = "account_number")
    var accountNumber: String,
    @Json(name = "beneficiary_name")
    var beneficiaryName: String,
    var amount: Double,
    var currency: String,
    var reference: String,
    @Json(name = "bank_code")
    var bankCode: String
)