package com.shengshijie.servertest.requset

import com.shengshijie.servertest.util.getParamSign

class EmptyRequest: BaseRequest() {
     var sign = getParamSign(this)
}
