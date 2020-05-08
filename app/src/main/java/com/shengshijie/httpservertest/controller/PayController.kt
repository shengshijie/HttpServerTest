package com.shengshijie.httpservertest.controller

import com.shengshijie.server.http.RawResponse
import com.shengshijie.server.http.RawResponse.Companion.ok
import com.shengshijie.server.http.annotation.Controller
import com.shengshijie.server.http.annotation.RequestMapping

@Controller
@RequestMapping(value = "/pay")
class PayController {
    @RequestMapping(value = "/setAmount", method = "GET")
    fun hello(amount: String, name: String, age: String): RawResponse<Any> {
        return ok("$amount|$name|$age")
    }

    @RequestMapping(value = "/init", method = "POST")
    fun init(name: String): RawResponse<Any> {
        return ok("hello2")
    }

    @RequestMapping(value = "/start", method = "POST")
    fun startPay(name: String): RawResponse<Any>? {
        return ok("hello2")
    }

    @RequestMapping(value = "/destroy", method = "POST")
    fun stopPay(name: String): RawResponse<Any>? {
        return ok("hello2")
    }

}