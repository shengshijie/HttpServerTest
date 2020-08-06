package com.shengshijie.servertest.requset

import com.shengshijie.servertest.util.getParamSign

data class SetAmountRequest(var amount: String) : BaseRequest() {
    var sign = getParamSign(this)
}