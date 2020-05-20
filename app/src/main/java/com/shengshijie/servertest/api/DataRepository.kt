package com.shengshijie.servertest.api

import com.shengshijie.log.HLog
import com.shengshijie.servertest.requset.Post2Request
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response

@ExperimentalCoroutinesApi
class DataRepository constructor(private val apiService: TestService) {

    private suspend inline fun <T : BaseResponse> createFlow(crossinline request: suspend () -> Response<T>): Flow<State<T>> {
        return flow {
            val response = request()
            emit(State.loading<T>())
            if (response.isSuccessful && response.body() != null) {
                if (response.body()!!.code == 0) {
                    emit(State.success(response.body() as T))
                } else {
                    emit(State.error<T>(response.body()!!.message))
                }
            } else {
                emit(State.error<T>(response.message()))
            }
        }.flowOn(Dispatchers.IO)
                .catch {
                    HLog.e(it)
                    emit(State.error("Network error:" + it.message))
                }
    }

    suspend fun get1(amount: Double) = createFlow {
        apiService.get1(amount)
    }

    suspend fun get2() = createFlow {
        apiService.get2()
    }

    suspend fun post1(name: String,age: String,amount: String) = createFlow {
        apiService.post1(Post2Request().apply {
            this.name = name
            this.age = age
            this.amount = amount
        })
    }

    suspend fun post2() = createFlow {
        apiService.post2()
    }

    private fun generateRequestBody(requestDataMap: Map<String, String>): HashMap<String, RequestBody> {
        val requestBodyMap: HashMap<String, RequestBody> = HashMap()
        requestDataMap.forEach {
            requestBodyMap[it.key] = it.value.toRequestBody()
        }
        return requestBodyMap
    }

}