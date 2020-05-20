package com.shengshijie.server.http

import io.netty.handler.ssl.SslContext
import io.netty.handler.ssl.SslContextBuilder
import io.netty.handler.ssl.util.SelfSignedCertificate
import java.security.cert.CertificateException
import javax.net.ssl.SSLException

object SslContextFactory {

    fun createSslContext(): SslContext {
        return try {
            val cert = SelfSignedCertificate()
            SslContextBuilder.forServer(cert.certificate(), cert.privateKey()).build()
        } catch (e: CertificateException) {
            throw RuntimeException(e)
        } catch (e: SSLException) {
            throw RuntimeException(e)
        }
    }
}