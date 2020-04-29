package com.shengshijie.httpserver.http

import io.netty.handler.ssl.SslContext
import io.netty.handler.ssl.SslContextBuilder
import io.netty.handler.ssl.util.SelfSignedCertificate
import java.security.cert.CertificateException
import javax.net.ssl.SSLException

object SslContextFactory {

    fun createSslContext(): SslContext {
         return try {
            val cert = SelfSignedCertificate()
            SslContextBuilder.forServer(cert.key(), cert.cert()).build()
        } catch (e: CertificateException) {
            throw RuntimeException(e)
        } catch (e: SSLException) {
            throw RuntimeException(e)
        }
    }
}