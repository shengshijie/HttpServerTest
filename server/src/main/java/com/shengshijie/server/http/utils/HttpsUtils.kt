package com.shengshijie.server.http.utils

import io.netty.channel.socket.SocketChannel
import io.netty.handler.ssl.SslContext
import io.netty.handler.ssl.SslContextBuilder
import io.netty.handler.ssl.SslHandler
import io.netty.handler.ssl.util.SelfSignedCertificate
import java.io.FileInputStream
import java.security.KeyStore
import java.security.cert.CertificateException
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLEngine
import javax.net.ssl.SSLException

internal object HttpsUtils {

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

    @Throws(Exception::class)
    fun createSSlContext(): SSLContext {
        val passArray = "123456".toCharArray()
        val sslContext: SSLContext = SSLContext.getInstance("TLSv1")
        val ks: KeyStore = KeyStore.getInstance("JKS")
        val inputStream = FileInputStream("i:/server.jks")
        ks.load(inputStream, passArray)
        val kmf: KeyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())
        kmf.init(ks, passArray)
        sslContext.init(kmf.keyManagers, null, null)
        inputStream.close()
        return sslContext
    }

    fun getSSlHandler(ch: SocketChannel): SslHandler {
        return createSslContext().newHandler(ch.alloc())
    }

    fun getSslHandler(): SslHandler {
        val sslEngine: SSLEngine = createSSlContext().createSSLEngine()
        sslEngine.useClientMode = false
        return SslHandler(sslEngine)
    }

}