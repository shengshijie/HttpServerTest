package com.shengshijie.servertest.requset

import com.shengshijie.servertest.util.getParamSign

data class StartRequest(var orderNumber: String, var instant: Boolean) : BaseRequest() {
    var sign = getParamSign(this)
}

