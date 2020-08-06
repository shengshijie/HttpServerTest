package com.shengshijie.servertest.requset

import com.shengshijie.servertest.util.getParamSign

data class SetFaceResultRequest(var faceBase64: String, var userName: String, var userNumber: String, var similarity: String, var threshold: String, var captureTime: String) : BaseRequest() {
    var sign = getParamSign(this)
}

