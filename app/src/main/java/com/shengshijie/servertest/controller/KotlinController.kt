package com.shengshijie.servertest.controller

import com.shengshijie.server.http.RawResponse
import com.shengshijie.server.http.RawResponse.Companion.ok
import com.shengshijie.server.http.annotation.Controller
import com.shengshijie.server.http.annotation.Param
import com.shengshijie.server.http.annotation.RequestMapping

@Controller
@RequestMapping(value = "/kotlin")
class KotlinController {
    @RequestMapping(value = "/post1", method = "POST")
    fun post1(@Param(value = "amount") amount: String, name: String, age: String): RawResponse<Any> {
        return ok("$amount|$name|$age")
    }

    @RequestMapping(value = "/post2", method = "POST")
    fun post2(): RawResponse<Any> {
        return ok("post2")
    }

    @RequestMapping(value = "/get1", method = "GET")
    fun get1(amount: String): RawResponse<Any>? {
        return ok("amount:$amount")

    }

    @RequestMapping(value = "/get2", method = "GET")
    fun get2(): RawResponse<Any>? {
        return ok("get2")
    }

}