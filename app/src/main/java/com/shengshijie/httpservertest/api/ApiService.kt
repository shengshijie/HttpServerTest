package com.shengshijie.httpservertest.api

import com.shengshijie.httpservertest.requset.BaseRequest
import com.shengshijie.httpservertest.requset.PasswordRequest
import com.shengshijie.httpservertest.requset.SetAmountRequest
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("api/pay/init")
    suspend fun init(@Body request: BaseRequest): Response<BaseResponse>

    @POST("api/pay/setAmount")
    suspend fun setAmount(@Body request: SetAmountRequest): Response<SetAmountResponse>

    @POST("api/pay/start")
    suspend fun start(@Body request: BaseRequest): Response<BaseResponse>

    @POST("api/pay/verifyPassword")
    suspend fun verifyPassword(@Body request: PasswordRequest): Response<BaseResponse>

    @POST("api/pay/cancel")
    suspend fun cancel(@Body request: BaseRequest): Response<BaseResponse>

    @POST("api/pay/destroy")
    suspend fun destroy(@Body request: BaseRequest): Response<BaseResponse>

}

//const val BASE_URL = "http://192.168.88.137:8888"
const val BASE_URL = "http://localhost:8888"