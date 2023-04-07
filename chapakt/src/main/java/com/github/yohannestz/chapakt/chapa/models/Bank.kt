package com.github.yohannestz.chapakt.chapa.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Json(name = "data")
@JsonClass(generateAdapter = true)
data class Bank(
    val id: String,
    val swift: String,
    val name: String,
    @Json(name = "acct_length")
    val accountLength: Int,
    @Json(name = "country_id")
    val countryId: Int,
    @Json(name = "created_at")
    val createdAt: String,
    @Json(name = "updated_at")
    val updatedAt: String,
    @Json(name = "is_mobilemoney")
    val isMobileMoney: Int?,
    val currency: String,
)