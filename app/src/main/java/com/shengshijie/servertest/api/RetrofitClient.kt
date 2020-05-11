package com.shengshijie.servertest.api

import com.google.gson.GsonBuilder
import com.shengshijie.log.HLog
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    fun getService(): ApiService = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .baseUrl(BASE_URL)
            .client(OkHttpClient
                    .Builder()
                    .readTimeout(24L,TimeUnit.HOURS)
                    .addInterceptor(HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                        override fun log(message: String) {
                            HLog.i(message)
                        }
                    }).apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
                    .build()
            )
            .build()
            .create(ApiService::class.java)

}