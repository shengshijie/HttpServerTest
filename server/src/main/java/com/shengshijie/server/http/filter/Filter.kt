package com.shengshijie.server.http.filter

import com.shengshijie.server.http.IHttpRequest
import com.shengshijie.server.http.IHttpResponse

interface Filter {

    fun preFilter(request: IHttpRequest, response: IHttpResponse): Boolean {
        return true
    }

    fun postFilter(request: IHttpRequest, response: IHttpResponse) {}

}