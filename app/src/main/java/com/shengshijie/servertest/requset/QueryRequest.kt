package com.shengshijie.servertest.requset

import com.shengshijie.servertest.util.getParamSign

data class QueryRequest(var orderNumber: String?) : BaseRequest(){
    var sign = getParamSign(this)
}