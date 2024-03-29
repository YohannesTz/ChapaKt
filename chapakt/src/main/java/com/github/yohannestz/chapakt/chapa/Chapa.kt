package com.github.yohannestz.chapakt.chapa

import com.github.yohannestz.chapakt.chapa.data.remote.ChapaApiClientBuilder
import com.github.yohannestz.chapakt.chapa.data.remote.ChapaApiService
import com.github.yohannestz.chapakt.chapa.models.*
import com.github.yohannestz.chapakt.chapa.util.ChapaValidator
import java.util.*

open class Chapa(private val secretKey: String) {

    private val chapaApiService: ChapaApiService = ChapaApiClientBuilder.getClient(secretKey).create(ChapaApiService::class.java)

    suspend fun initialize(postData: ChapaPostData): ChapaPayment? {
        var chapaInitResult: ChapaPayment? = null
        if (ChapaValidator.isValid(postData)) {
            chapaInitResult = chapaApiService.initializeMobilePayment(postData).body()
        }
        return chapaInitResult
    }

    suspend fun verifyTransaction(txRef: String): ChapaResponse? {
        return chapaApiService.verifyTransaction(txRef).body()
    }

    suspend fun getListOfBanks(): List<Bank> {
        val getBanksResponse: GetBanksResponse? = chapaApiService.getListOfSupportedBanks().body()
        return getBanksResponse?.data ?: emptyList()
    }

    suspend fun createSubAccount(subAccount: SubAccount): CreateSubAccountResponse? {
        return chapaApiService.createSubAccount(subAccount).body()
    }

    suspend fun initTransfer(transfer: Transfer): InitTransferResponse? {
        return chapaApiService.initTransfer(transfer).body()
    }

    fun getFormattedTxRef(): String {
        return "txRef_${UUID.randomUUID().toString()}"
    }
}