package com.shengshijie.servertest.api

import com.shengshijie.servertest.requset.*
import com.shengshijie.servertest.response.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("api/pay/init")
    suspend fun init(@Body request: EmptyRequest): BaseResponse<Unit>

    @POST("api/pay/setAmount")
    suspend fun setAmount(@Body request: SetAmountRequest): BaseResponse<SetAmountResponse>

    @POST("api/pay/start")
    suspend fun start(@Body request: StartRequest): BaseResponse<PayResultResponse>

    @POST("api/pay/changeAmount")
    suspend fun changeAmount(@Body request: ChangeAmountRequest): BaseResponse<Unit>

    @POST("api/pay/verifyPassword")
    suspend fun verifyPassword(@Body request: PasswordRequest): BaseResponse<PayResultResponse>

    @POST("api/pay/cancel")
    suspend fun cancel(@Body request: EmptyRequest): BaseResponse<Unit>

    @POST("api/pay/destroy")
    suspend fun destroy(@Body request: EmptyRequest): BaseResponse<Unit>

    @POST("api/media/setFaceResult")
    suspend fun setFaceResult(@Body request: SetFaceResultRequest): BaseResponse<Unit>

    @POST("api/order/list")
    suspend fun order(@Body request: QueryRequest): BaseResponse<QueryResponse>

    @POST("api/order/refund")
    suspend fun refund(@Body request: QueryRequest): BaseResponse<Unit>

    @POST("api/pay/query")
    suspend fun query(@Body request: QueryRequest): BaseResponse<PayResultResponse>

    @POST("api/person/detail")
    suspend fun detail(@Body request: EmptyRequest): BaseResponse<PersonResponse>

}

const val BASE_URL = "http://192.168.31.166:8181"
//const val BASE_URL = "http://localhost:8888"