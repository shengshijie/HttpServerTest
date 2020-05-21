package com.shengshijie.servertest.api

import java.security.SecureRandom
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


object SSLFactory {

    fun getSSLSocketFactory(): SSLSocketFactory {
        val trustAllCerts: Array<TrustManager> = arrayOf(object : X509TrustManager {

            override fun checkClientTrusted(chain: Array<out java.security.cert.X509Certificate>?, authType: String?) {

            }

            override fun checkServerTrusted(chain: Array<out java.security.cert.X509Certificate>?, authType: String?) {

            }

            override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate?> = arrayOfNulls(0)

        })
        val sslContext: SSLContext = SSLContext.getInstance("TLS")
        sslContext.init(null, trustAllCerts, SecureRandom())
        return sslContext.socketFactory
    }



}