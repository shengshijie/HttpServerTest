package com.shengshijie.servertest.api

import com.google.gson.GsonBuilder
import com.shengshijie.log.HLog
import com.shengshijie.servertest.SPHelper
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.X509TrustManager

object RetrofitClient {

    fun getService(): ApiService = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .baseUrl(SPHelper.ip)
            .client(OkHttpClient
                    .Builder()
                    .readTimeout(24L, TimeUnit.HOURS)
                    .addInterceptor(HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                        override fun log(message: String) {
                            HLog.i(message)
                        }
                    }).apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
                    .sslSocketFactory(SSLFactory.getSSLSocketFactory(), object : X509TrustManager {

                        override fun checkClientTrusted(chain: Array<out java.security.cert.X509Certificate>?, authType: String?) {

                        }

                        override fun checkServerTrusted(chain: Array<out java.security.cert.X509Certificate>?, authType: String?) {

                        }

                        override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate?> = arrayOfNulls(0)

                    })
                    .hostnameVerifier(HostnameVerifier { _, _ -> true })
                    .build()
            )
            .build()
            .create(ApiService::class.java)

}