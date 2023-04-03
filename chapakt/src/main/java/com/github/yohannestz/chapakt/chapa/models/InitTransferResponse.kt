package com.github.yohannestz.chapakt.chapa.models

import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class InitTransferResponse(
    val data: String,
    val message: String,
    val status: String
):Serializable