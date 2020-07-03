package com.shengshijie.servertest.response

open class BaseResponse<T>{

    var code: Int = 0
    var message: String = ""
    var data: T? = null

}