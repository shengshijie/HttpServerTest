package com.shengshijie.httpserver

import io.netty.buffer.ByteBuf
import io.netty.handler.codec.DecoderResult
import io.netty.handler.codec.http.FullHttpRequest
import io.netty.handler.codec.http.HttpHeaders
import io.netty.handler.codec.http.HttpMethod
import io.netty.handler.codec.http.HttpVersion
import java.nio.charset.Charset
import java.util.*

class HttpRequest(private val realRequest: FullHttpRequest) : FullHttpRequest {
    fun contentText(): String {
        return content().toString(Charset.forName("UTF-8"))
    }

    fun getLongPathValue(index: Int): Long {
        val paths = uri().split("/").toTypedArray()
        return paths[index].toLong()
    }

    fun getStringPathValue(index: Int): String {
        val paths = uri().split("/").toTypedArray()
        return paths[index]
    }

    fun getIntPathValue(index: Int): Int {
        val paths = uri().split("/").toTypedArray()
        return paths[index].toInt()
    }

    fun isAllowed(method: String?): Boolean {
        return getMethod().name().equals(method, ignoreCase = true)
    }

    fun matched(path: String?, equal: Boolean): Boolean {
        val uri = uri()
        return if (equal) path == uri else uri.startsWith(path!!)
    }

    override fun content(): ByteBuf {
        return realRequest.content()
    }

    override fun trailingHeaders(): HttpHeaders {
        return realRequest.trailingHeaders()
    }

    override fun copy(): FullHttpRequest {
        return realRequest.copy()
    }

    override fun duplicate(): FullHttpRequest {
        return realRequest.duplicate()
    }

    override fun retainedDuplicate(): FullHttpRequest {
        return realRequest.retainedDuplicate()
    }

    override fun replace(byteBuf: ByteBuf): FullHttpRequest {
        return realRequest.replace(byteBuf)
    }

    override fun retain(i: Int): FullHttpRequest {
        return realRequest.retain(i)
    }

    override fun refCnt(): Int {
        return realRequest.refCnt()
    }

    override fun retain(): FullHttpRequest {
        return realRequest.retain()
    }

    override fun touch(): FullHttpRequest {
        return realRequest.touch()
    }

    override fun touch(o: Any): FullHttpRequest {
        return realRequest.touch(o)
    }

    override fun release(): Boolean {
        return realRequest.release()
    }

    override fun release(i: Int): Boolean {
        return realRequest.release(i)
    }

    override fun getProtocolVersion(): HttpVersion {
        return realRequest.protocolVersion()
    }

    override fun protocolVersion(): HttpVersion {
        return realRequest.protocolVersion()
    }

    override fun setProtocolVersion(httpVersion: HttpVersion): FullHttpRequest {
        return realRequest.setProtocolVersion(httpVersion)
    }

    override fun headers(): HttpHeaders {
        return realRequest.headers()
    }

    override fun getMethod(): HttpMethod {
        return realRequest.method()
    }

    override fun method(): HttpMethod {
        return realRequest.method()
    }

    override fun setMethod(httpMethod: HttpMethod): FullHttpRequest {
        return realRequest.setMethod(httpMethod)
    }

    override fun getUri(): String {
        return realRequest.uri()
    }

    override fun uri(): String {
        return realRequest.uri()
    }

    override fun setUri(s: String): FullHttpRequest {
        return realRequest.setUri(s)
    }

    override fun getDecoderResult(): DecoderResult {
        return realRequest.decoderResult()
    }

    override fun decoderResult(): DecoderResult {
        return realRequest.decoderResult()
    }

    override fun setDecoderResult(decoderResult: DecoderResult) {
        realRequest.decoderResult = decoderResult
    }

}