package com.shengshijie.servertest.requset

import com.shengshijie.servertest.util.getParamSign

data class TestRequest(var name: String = "", var age: Int = 0, var amount: Double = 1.0) {
    var sign = getParamSign(this)
}