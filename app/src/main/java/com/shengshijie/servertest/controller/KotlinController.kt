package com.shengshijie.servertest.controller

import com.shengshijie.server.http.annotation.Controller
import com.shengshijie.server.http.annotation.Param
import com.shengshijie.server.http.annotation.RequestMapping
import com.shengshijie.server.http.exception.BusinessException
import com.shengshijie.server.http.response.RawResponse
import com.shengshijie.servertest.Person

@Controller
@RequestMapping(value = "/kotlin")
class KotlinController {
    @RequestMapping(value = "/post1", method = "POST")
    fun post1(@Param(value = "amount") amount: String, name: String, age: String): Any {
        return "$amount|$name|$age"
    }

    @RequestMapping(value = "/post2", method = "POST")
    fun post2() {
        throw BusinessException("dingdan", 205)
    }

    @RequestMapping(value = "/get1", method = "GET")
    fun get1(amount: String): Any {
        return RawResponse(Person(null,"34"))
    }

    @RequestMapping(value = "/get2", method = "GET")
    fun get2(): Any {
        return RawResponse(null)
    }

}