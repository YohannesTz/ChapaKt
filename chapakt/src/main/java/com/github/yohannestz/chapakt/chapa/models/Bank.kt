package com.github.yohannestz.chapakt.chapa.models

import com.squareup.moshi.Json
import java.time.LocalDateTime

@Json(name = "data")
data class Bank(
    val id: String,
    val name: String,
    val acct_length: Int,
    val country_id: Int,
    val currency: String,
    @Json(name = "is_mobilemoney")
    val isMobileMoney: Int,
    val swift: String,
    @Json(name = "country_id")
    val countryId: String,
    @Json(name = "created_at")
    val createdAt: String,
    @Json(name = "updated_at")
    val updatedAt: String
)