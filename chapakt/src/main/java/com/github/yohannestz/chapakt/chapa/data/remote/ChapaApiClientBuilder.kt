package com.github.yohannestz.chapakt.chapa.data.remote

import com.github.yohannestz.chapakt.chapa.util.ChapaConstants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


object ChapaApiClientBuilder {

    fun getClient(secretKey: String): Retrofit {
        val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()

        httpClient.addInterceptor(Interceptor { chain ->
            val request: Request =
                chain.request()
                    .newBuilder()
                    .addHeader("Authorization ", "Bearer $secretKey")
                    .addHeader("Accept-Encoding", "application/json")
                    .build()
            chain.proceed(request)
        })

        return Retrofit.Builder()
            .baseUrl(ChapaConstants.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }
}