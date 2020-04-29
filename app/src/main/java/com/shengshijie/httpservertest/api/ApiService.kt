package com.shengshijie.httpservertest.api

import com.shengshijie.httpservertest.controller.requset.BaseRequest
import com.shengshijie.httpservertest.controller.requset.PasswordRequest
import com.shengshijie.httpservertest.controller.requset.SetAmountRequest
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("pay/init")
    suspend fun init(@Body request: BaseRequest): Response<BaseResponse>

    @POST("pay/setAmount")
    suspend fun setAmount(@Body request: SetAmountRequest): Response<SetAmountResponse>

    @POST("pay/start")
    suspend fun start(@Body request: BaseRequest): Response<BaseResponse>

    @POST("pay/verifyPassword")
    suspend fun verifyPassword(@Body request: PasswordRequest): Response<BaseResponse>

    @POST("pay/cancel")
    suspend fun cancel(@Body request: BaseRequest): Response<BaseResponse>

    @POST("pay/destroy")
    suspend fun destroy(@Body request: BaseRequest): Response<BaseResponse>

}

const val BASE_URL = "http://192.168.88.154:8888"