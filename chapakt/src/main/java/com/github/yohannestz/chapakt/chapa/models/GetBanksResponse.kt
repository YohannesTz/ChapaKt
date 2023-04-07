package com.github.yohannestz.chapakt.chapa.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetBanksResponse(
    val data: List<Bank>,
    val message: String,
    val status: String?
)