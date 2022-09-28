package com.github.yohannestz.chapakt.chapa

import android.util.Log
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.net.URL
import java.util.*

open class Chapa(private val secretKey: String) {

    private var client: OkHttpClient = OkHttpClient()
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val chapaPaymentJsonAdapter = moshi.adapter(ChapaPayment::class.java)
    private val chapaPaymentResultAdapter = moshi.adapter(ChapaResponse::class.java)

    fun initialize(postData: ChapaPostData): ChapaPayment? {
        var chapaInitResult: ChapaPayment? = null
        if (ChapaValidator.isValid(postData)) {
            val url = URL(ChapaConstants.baseMobileUrl)
            val jsonBody = JSONObject()
            jsonBody.put("amount", postData.amount.toString())
            jsonBody.put("currency", postData.currency.uppercase(Locale.getDefault()))
            jsonBody.put("email", postData.email)
            jsonBody.put("first_name", postData.firstName)
            jsonBody.put("last_name", postData.lastName)
            jsonBody.put("tx_ref", postData.txRef)
            jsonBody.put("return_url", postData.returnUrl)
            jsonBody.put("customization[title]", postData.customizationTitle)
            jsonBody.put("customization[description]", postData.customizationDescription)

            val mediaType = "application/json".toMediaType()
            val requestBody = jsonBody.toString().toRequestBody(mediaType)

            val request = Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer $secretKey")
                .addHeader("Accept-Encoding", "application/json")
                .post(requestBody)
                .build()

            client.newCall(request).execute().use { response ->
                val responseBody = response.body!!.source()
                if (response.isSuccessful) {
                    chapaInitResult = chapaPaymentJsonAdapter.fromJson(responseBody)
                } else {
                    if (response.message.isNotEmpty()) {
                        throw ChapaException(response.message)
                    } else {
                        throw ChapaException("Something was wrong")
                    }
                }
            }
        }
        return chapaInitResult
    }

    fun verifyPayment(txRef: String): ChapaResponse? {
        var paymentResponse: ChapaResponse? = null
        val request = Request.Builder()
            .url("${ChapaConstants.verifyTransaction}$txRef")
            .addHeader("Authorization", "Bearer $secretKey")
            .build()

        client.newCall(request).execute().use { response ->
            val responseBody = response.body!!.source()
            if (response.isSuccessful) {
                paymentResponse = chapaPaymentResultAdapter.fromJson(responseBody)
            } else {
                if (response.message.isNotEmpty()) {
                    throw ChapaException(response.message)
                } else {
                    throw ChapaException("Something was wrong")
                }
            }
        }

        return paymentResponse
    }

}