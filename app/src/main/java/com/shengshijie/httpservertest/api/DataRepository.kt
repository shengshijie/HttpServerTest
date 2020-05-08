package com.shengshijie.httpservertest.api

import com.shengshijie.httpservertest.requset.BaseRequest
import com.shengshijie.httpservertest.requset.PasswordRequest
import com.shengshijie.httpservertest.requset.SetAmountRequest
import com.shengshijie.log.HLog
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
class DataRepository constructor(private val apiService: ApiService) {

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

    suspend fun init() = createFlow {
        apiService.init(BaseRequest())
    }

    suspend fun setAmount(amount: Double) = createFlow {
        apiService.setAmount(SetAmountRequest().apply { this.amount = amount })
    }

    suspend fun start() = createFlow {
        apiService.start(BaseRequest())
    }

    suspend fun verifyPassword(password: String) = createFlow {
        apiService.verifyPassword(PasswordRequest().apply { this.password = password })
    }

    suspend fun cancel() = createFlow {
        apiService.cancel(BaseRequest())
    }

    suspend fun destroy() = createFlow {
        apiService.destroy(BaseRequest())
    }

    private fun generateRequestBody(requestDataMap: Map<String, String>): HashMap<String, RequestBody> {
        val requestBodyMap: HashMap<String, RequestBody> = HashMap()
        requestDataMap.forEach {
            requestBodyMap[it.key] = it.value.toRequestBody()
        }
        return requestBodyMap
    }

}