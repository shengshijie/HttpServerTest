package com.shengshijie.httpservertest.controller

import com.google.gson.Gson
import com.shengshijie.httpservertest.requset.SetAmountRequest
import com.shengshijie.server.http.RawResponse
import com.shengshijie.server.http.RawResponse.Companion.fail
import com.shengshijie.server.http.RawResponse.Companion.ok
import com.shengshijie.server.http.annotation.Controller
import com.shengshijie.server.http.annotation.RequestMapping
import io.netty.handler.codec.http.FullHttpRequest
import java.nio.charset.Charset

@Controller
@RequestMapping(value = "/pay")
class PayController{
    @RequestMapping(value = "/setAmount", method = "POST")
    fun hello(request: FullHttpRequest): RawResponse<Any> {
        val setAmountRequest = Gson().fromJson(request.content().toString(Charset.forName("UTF-8")), SetAmountRequest::class.java)
        return if (setAmountRequest.amount > 10) {
            ok("hello")
        } else {
            fail("ddd")
        }
    }

    @RequestMapping(value = "/init", method = "POST")
    fun init(request: FullHttpRequest): RawResponse<Any> {
        return ok("hello2")
    }

    @RequestMapping(value = "/start", method = "POST")
    fun startPay(request: FullHttpRequest): RawResponse<Any>? {
        return ok("hello2")
    }

    @RequestMapping(value = "/destroy", method = "POST")
    fun stopPay(request: FullHttpRequest): RawResponse<Any>? {
        return ok("hello2")
    }

}