package com.github.yohannestz.chapakt.chapa.util

import com.github.yohannestz.chapakt.chapa.models.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
    private val getSupportedBanksAdapter = moshi.adapter(GetBanksResponse::class.java)
    private val initializeTransferAdapter = moshi.adapter(InitTransferResponse::class.java)

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

            val customization = Customization(
                postData.customizationTitle,
                postData.customizationDescription,
                postData.customizationLogo
            )

            //jsonBody.put("customization[title]", postData.customizationTitle)
            //jsonBody.put("customization[description]", postData.customizationDescription)
            jsonBody.put("customization", customization)

            val mediaType = "application/json".toMediaType()
            val requestBody = jsonBody.toString().toRequestBody(mediaType)

            val request = Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer $secretKey")
                .addHeader("Accept-Encoding", "application/json")
                .post(requestBody)
                .build()

            CoroutineScope(Dispatchers.IO).launch {
                client.newCall(request).execute().use { response ->
                    val responseBody = response.body!!.source()
                    chapaInitResult = chapaPaymentJsonAdapter.fromJson(responseBody)
                    if (!response.isSuccessful) {
                        throw ChapaException(chapaInitResult!!.message)
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
                    throw ChapaException("Something was wrong!")
                }
            }
        }

        return paymentResponse
    }

    fun getListOfBanks(): List<Bank> {
        var getBanksResponse: GetBanksResponse? = null
        val getBanksRequest = Request.Builder()
            .url(ChapaConstants.getSupportedBanksUrl)
            .addHeader("Authorization", "Bearer $secretKey")
            .build()

        client.newCall(getBanksRequest).execute().use { response ->
            val responseBody = response.body!!.source()
            if (response.isSuccessful) {
                getBanksResponse = getSupportedBanksAdapter.fromJson(responseBody)
            } else {
                if (response.message.isNotEmpty()) {
                    throw ChapaException(response.message)
                } else {
                    throw ChapaException("Something was wrong!")
                }
            }
        }

        return getBanksResponse?.data ?: emptyList()
    }

    fun createSubAccount(subAccount: SubAccount): Chapa {
        val jsonBody = JSONObject()
        jsonBody.put("business_name", subAccount.businessName)
        jsonBody.put("account_name", subAccount.accountName)
        jsonBody.put("bank_code", subAccount.bankCode)
        jsonBody.put("split_type", subAccount.splitType)
        jsonBody.put("split_value", subAccount.splitValue)

        val mediaType = "application/json".toMediaType()
        val requestBody = jsonBody.toString().toRequestBody(mediaType)

        val createSubAccountRequest = Request.Builder()
            .url(ChapaConstants.subAccountUrl)
            .addHeader("Authorization", "Bearer $secretKey")
            .addHeader("Accept-Encoding", "application/json")
            .post(requestBody)
            .build()

        client.newCall(createSubAccountRequest).execute().use { response ->
            val responseBody = response.body!!.source()
            if (response.isSuccessful) {
                //todo
            } else {
                if (response.message.isNotEmpty()) {
                    throw ChapaException(response.message)
                } else {
                    throw ChapaException("Something was wrong!")
                }
            }
        }
        return this
    }

    fun initTransfer(transfer: Transfer): InitTransferResponse? {
        var initTransferResponse: InitTransferResponse? = null
        val jsonBody = JSONObject()
        jsonBody.put("account_name", transfer.accountNumber)
        jsonBody.put("account_number", transfer.accountNumber)
        jsonBody.put("amount", transfer.amount)
        jsonBody.put("currency", transfer.currency)
        jsonBody.put("beneficiary_name", transfer.beneficiaryName)
        jsonBody.put("reference", transfer.reference)
        jsonBody.put("bank_code", transfer.bankCode)

        val mediaType = "application/json".toMediaType()
        val requestBody = jsonBody.toString().toRequestBody(mediaType)

        val initializeTransfer = Request.Builder()
            .url(ChapaConstants.initiateTransfer)
            .addHeader("Authorization", "Bearer $secretKey")
            .addHeader("Accept-Encoding", "application/json")
            .post(requestBody)
            .build()

        client.newCall(initializeTransfer).execute().use { response ->
            val responseBody = response.body!!.source()
            if (response.isSuccessful) {
                initTransferResponse = initializeTransferAdapter.fromJson(responseBody)
            } else {
                if (response.message.isNotEmpty()) {
                    throw ChapaException(response.message)
                } else {
                    throw ChapaException("Something was wrong!")
                }
            }
        }

        return initTransferResponse
    }
}