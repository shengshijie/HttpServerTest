package com.shengshijie.servertest.api

import com.shengshijie.servertest.requset.*
import com.shengshijie.servertest.response.BaseResponse
import com.shengshijie.servertest.response.PayResultResponse
import com.shengshijie.servertest.response.QueryResponse
import com.shengshijie.servertest.response.SetAmountResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("api/pay/init")
    suspend fun init(@Body request: BaseRequest): Response<BaseResponse<Unit>>

    @POST("api/pay/setAmount")
    suspend fun setAmount(@Body request: SetAmountRequest): Response<BaseResponse<SetAmountResponse>>

    @POST("api/pay/start")
    suspend fun start(@Body request: StartRequest): Response<BaseResponse<PayResultResponse>>

    @POST("api/pay/changeAmount")
    suspend fun changeAmount(@Body request: ChangeAmountRequest): Response<BaseResponse<Unit>>

    @POST("api/pay/verifyPassword")
    suspend fun verifyPassword(@Body request: PasswordRequest): Response<BaseResponse<PayResultResponse>>

    @POST("api/pay/cancel")
    suspend fun cancel(@Body request: BaseRequest): Response<BaseResponse<Unit>>

    @POST("api/pay/destroy")
    suspend fun destroy(@Body request: BaseRequest): Response<BaseResponse<Unit>>

    @POST("api/media/setFaceResult")
    suspend fun setFaceResult(@Body request: SetFaceResultRequest): Response<BaseResponse<Unit>>

    @POST("api/query/order")
    suspend fun order(@Body request: QueryRequest): Response<BaseResponse<QueryResponse>>

    @POST("api/pay/query")
    suspend fun query(@Body request: QueryRequest): Response<BaseResponse<PayResultResponse>>

}

const val BASE_URL = "http://192.168.31.166:8181"
//const val BASE_URL = "http://localhost:8888"