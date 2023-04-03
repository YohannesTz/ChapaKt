package com.github.yohannestz.chapakt.chapa.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class CreateSubAccountResponse(
    val data: SubAccountData,
    val message: String,
    val status: String
): Serializable

@Json(name = "data")
data class SubAccountData(
    @Json(name = "subaccounts[id]")
    val id: String
)