package com.shengshijie.servertest.requset

import java.util.*

open class BaseRequest {

    var nonce = UUID.randomUUID().toString()
    var timestamp = "${System.currentTimeMillis()}"

}