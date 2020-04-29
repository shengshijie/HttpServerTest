package com.shengshijie.httpservertest.controller

import com.google.auto.service.AutoService
import com.google.gson.Gson
import com.shengshijie.httpserver.http.*
import com.shengshijie.httpserver.http.RawResponse.Companion.fail
import com.shengshijie.httpserver.http.RawResponse.Companion.ok
import com.shengshijie.httpservertest.controller.requset.SetAmountRequest

@AutoService(IController::class)
@Controller
@RequestMapping(value = "/pay")
class PayController : IController {
    @RequestMapping(value = "/setAmount", method = "POST")
    fun hello(request: HttpFullRequest): RawResponse<Any> {
        val setAmountRequest = Gson().fromJson(request.contentText(), SetAmountRequest::class.java)
        return if (setAmountRequest.amount > 10) {
            ok("hello")
        } else {
            fail("ddd")
        }
    }

    @RequestMapping(value = "/init", method = "POST")
    fun init(request: HttpFullRequest): RawResponse<Any> {
        return ok("hello2")
    }

    @RequestMapping(value = "/start", method = "POST")
    fun startPay(request: HttpFullRequest): RawResponse<Any>? {
        return ok("hello2")
    }

    @RequestMapping(value = "/destroy", method = "POST")
    fun stopPay(request: HttpFullRequest): RawResponse<Any>? {
        return ok("hello2")
    }

}