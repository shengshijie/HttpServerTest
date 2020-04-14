package com.shengshijie.httpserver

interface IFunctionHandler<T> {
    fun execute(request: HttpRequest?): RawResponse<T>
}