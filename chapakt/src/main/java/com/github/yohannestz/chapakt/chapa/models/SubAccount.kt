package com.github.yohannestz.chapakt.chapa.models

import com.github.yohannestz.chapakt.chapa.util.SplitType
import com.squareup.moshi.Json

data class SubAccount(
    @Json(name = "business_name")
    val businessName: String,
    @Json(name = "bank_code")
    val bankCode: String,
    @Json(name = "account_name")
    val accountName: String,
    @Json(name = "account_number")
    val accountNumber: String,
    @Json(name = "split_type")
    val splitType: SplitType,
    @Json(name = "split_value")
    val splitValue: Double
)
