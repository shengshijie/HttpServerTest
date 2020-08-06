package com.shengshijie.servertest.requset

import com.shengshijie.servertest.util.getParamSign

data class PasswordRequest(var password: String) : BaseRequest(){
    var sign = getParamSign(this)
}


