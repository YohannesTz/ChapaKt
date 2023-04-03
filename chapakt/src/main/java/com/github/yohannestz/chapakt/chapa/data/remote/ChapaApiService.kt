package com.github.yohannestz.chapakt.chapa.data.remote

import com.github.yohannestz.chapakt.chapa.models.*
import com.github.yohannestz.chapakt.chapa.util.ChapaConstants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ChapaApiService {
    @POST(ChapaConstants.BASE_MOBILE_INIT_URL)
    suspend fun initializeMobilePayment(@Body postData: ChapaPostData): Response<ChapaPayment>

    @GET(ChapaConstants.VERIFY_TRANSACTION + "/{txRef}")
    suspend fun verifyTransaction(@Path("txRef") txRef: String): Response<ChapaResponse>

    @GET(ChapaConstants.GET_SUPPORTED_BANKS)
    suspend fun getListOfSupportedBanks(): Response<GetBanksResponse>

    @POST(ChapaConstants.SUBACCOUNT_URL)
    suspend fun createSubAccount(@Body subAccount: SubAccount): Response<CreateSubAccountResponse>

    @POST(ChapaConstants.INITIATE_TRANSFER)
    suspend fun initTransfer(@Body transfer: Transfer): Response<InitTransferResponse>
}