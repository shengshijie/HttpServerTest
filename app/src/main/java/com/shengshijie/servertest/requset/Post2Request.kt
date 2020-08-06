package com.shengshijie.servertest.requset

import com.shengshijie.servertest.util.getParamSign

data class Post2Request(var name: String = "", var age: String = "", var amount: String = "") {
    var sign = getParamSign(this)
}