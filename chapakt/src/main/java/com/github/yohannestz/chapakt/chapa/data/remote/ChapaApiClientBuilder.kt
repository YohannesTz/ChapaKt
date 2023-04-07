package com.github.yohannestz.chapakt.chapa.data.remote

import com.github.yohannestz.chapakt.chapa.util.ChapaConstants
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


object ChapaApiClientBuilder {

    fun getClient(secretKey: String): Retrofit {
        val moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()

        val headerInterceptor = Interceptor { chain ->
            val request: Request =
                chain.request()
                    .newBuilder()
                    .addHeader("Authorization", "Bearer $secretKey")
                    .addHeader("Accept-Encoding", "application/json")
                    .build()
            chain.proceed(request)
        }
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(headerInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(ChapaConstants.BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }
}