package com.shengshijie.servertest.api

import com.shengshijie.servertest.requset.Post2Request
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface TestService {

    @GET("kotlin/get1")
    suspend fun get1(@Query(value = "amount") amount: Double): Response<SetAmountResponse>

    @GET("kotlin/get2")
    suspend fun get2(): Response<BaseResponse>

    @POST("kotlin/post1")
    suspend fun post1(@Body request: Post2Request): Response<BaseResponse>

    @POST("kotlin/post2")
    suspend fun post2(): Response<BaseResponse>

}

const val TEST_BASE_URL = "https://localhost:8888"