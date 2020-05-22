package com.shengshijie.server.http.filter

import com.shengshijie.server.http.request.IHttpRequest
import com.shengshijie.server.http.response.IHttpResponse

interface Filter {

    fun preFilter(request: IHttpRequest, response: IHttpResponse): Boolean {
        return true
    }

    fun postFilter(request: IHttpRequest, response: IHttpResponse) {}

}