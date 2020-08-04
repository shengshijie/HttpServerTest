package com.shengshijie.servertest.api

import com.google.gson.Gson
import com.shengshijie.log.HLog
import com.shengshijie.servertest.requset.*
import com.shengshijie.servertest.response.BaseResponse
import io.netty.buffer.ByteBufUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.security.MessageDigest
import java.util.*
import kotlin.collections.HashMap

@ExperimentalCoroutinesApi
class DataRepository {

    private suspend inline fun <T : BaseResponse<*>> createFlow(crossinline request: suspend () -> Response<T>): Flow<State<T>> {
        return flow {
            val response = request()
            emit(State.loading<T>())
            if (response.isSuccessful && response.body() != null) {
                if (response.body()!!.code == 1000) {
                    emit(State.success(response.body() as T))
                } else {
                    emit(State.error(response.body()!!.message))
                }
            } else {
                emit(State.error(response.message()))
            }
        }.flowOn(Dispatchers.IO)
                .catch {
                    HLog.e(it)
                    emit(State.error("Network error:" + it.message))
                }
    }

    suspend fun init() = createFlow {
        RetrofitClient.getService().init(BaseRequest().apply {
            this.nonce = UUID.randomUUID().toString()
            this.timestamp = "${System.currentTimeMillis()}"
            val gson = Gson()
            var map: MutableMap<String, String?> = mutableMapOf()
            map = gson.fromJson(gson.toJson(this), map.javaClass)
            this.sign = getParamSign(map)
        })
    }

    suspend fun setAmount(amount: String) = createFlow {
        RetrofitClient.getService().setAmount(SetAmountRequest().apply {
            this.amount = amount
            this.nonce = UUID.randomUUID().toString()
            this.timestamp = "${System.currentTimeMillis()}"
            val gson = Gson()
            var map: MutableMap<String, String?> = mutableMapOf()
            map = gson.fromJson(gson.toJson(this), map.javaClass)
            this.sign = getParamSign(map)
        })
    }

    suspend fun start(orderNumber: String, instant: Boolean) = createFlow {
        RetrofitClient.getService().start(StartRequest().apply {
            this.orderNumber = orderNumber
            this.instant = instant
            this.nonce = UUID.randomUUID().toString()
            this.timestamp = "${System.currentTimeMillis()}"
            val gson = Gson()
            var map: MutableMap<String, String?> = mutableMapOf()
            map = gson.fromJson(gson.toJson(this), map.javaClass)
            this.sign = getParamSign(map)
        })
    }

    suspend fun changeAmount(amount: String) = createFlow {
        RetrofitClient.getService().changeAmount(ChangeAmountRequest().apply {
            this.amount = amount
            this.nonce = UUID.randomUUID().toString()
            this.timestamp = "${System.currentTimeMillis()}"
            val gson = Gson()
            var map: MutableMap<String, String?> = mutableMapOf()
            map = gson.fromJson(gson.toJson(this), map.javaClass)
            this.sign = getParamSign(map)
        })
    }

    suspend fun setFaceResult(userName: String, userNumber: String) = createFlow {
        RetrofitClient.getService().setFaceResult(SetFaceResultRequest().apply {
            this.userName = userName
            this.userNumber = userNumber
            this.nonce = UUID.randomUUID().toString()
            this.timestamp = "${System.currentTimeMillis()}"
            val gson = Gson()
            var map: MutableMap<String, String?> = mutableMapOf()
            map = gson.fromJson(gson.toJson(this), map.javaClass)
            this.sign = getParamSign(map)
        })
    }

    suspend fun verifyPassword(password: String) = createFlow {
        RetrofitClient.getService().verifyPassword(PasswordRequest().apply {
            this.password = password
            this.nonce = UUID.randomUUID().toString()
            this.timestamp = "${System.currentTimeMillis()}"
            val gson = Gson()
            var map: MutableMap<String, String?> = mutableMapOf()
            map = gson.fromJson(gson.toJson(this), map.javaClass)
            this.sign = getParamSign(map)
        })
    }

    suspend fun cancel() = createFlow {
        RetrofitClient.getService().cancel(BaseRequest().apply {
            this.nonce = UUID.randomUUID().toString()
            this.timestamp = "${System.currentTimeMillis()}"
            val gson = Gson()
            var map: MutableMap<String, String?> = mutableMapOf()
            map = gson.fromJson(gson.toJson(this), map.javaClass)
            this.sign = getParamSign(map)
        })
    }

    suspend fun destroy() = createFlow {
        RetrofitClient.getService().destroy(BaseRequest().apply {
            this.nonce = UUID.randomUUID().toString()
            this.timestamp = "${System.currentTimeMillis()}"
            val gson = Gson()
            var map: MutableMap<String, String?> = mutableMapOf()
            map = gson.fromJson(gson.toJson(this), map.javaClass)
            this.sign = getParamSign(map)
        })
    }

    suspend fun order(orderNumber: String?) = createFlow {
        RetrofitClient.getService().order(QueryRequest().apply {
            this.orderNumber = orderNumber
            this.nonce = UUID.randomUUID().toString()
            this.timestamp = "${System.currentTimeMillis()}"
            val gson = Gson()
            var map: MutableMap<String, String?> = mutableMapOf()
            map = gson.fromJson(gson.toJson(this), map.javaClass)
            this.sign = getParamSign(map)
        })
    }

    suspend fun query(orderNumber: String?) = createFlow {
        RetrofitClient.getService().query(QueryRequest().apply {
            this.orderNumber = orderNumber
            this.nonce = UUID.randomUUID().toString()
            this.timestamp = "${System.currentTimeMillis()}"
            val gson = Gson()
            var map: MutableMap<String, String?> = mutableMapOf()
            map = gson.fromJson(gson.toJson(this), map.javaClass)
            this.sign = getParamSign(map)
        })
    }

    suspend fun detail() = createFlow {
        RetrofitClient.getService().detail(BaseRequest().apply {
            this.nonce = UUID.randomUUID().toString()
            this.timestamp = "${System.currentTimeMillis()}"
            val gson = Gson()
            var map: MutableMap<String, String?> = mutableMapOf()
            map = gson.fromJson(gson.toJson(this), map.javaClass)
            this.sign = getParamSign(map)
        })
    }

    private fun generateRequestBody(requestDataMap: Map<String, String>): HashMap<String, RequestBody> {
        val requestBodyMap: HashMap<String, RequestBody> = HashMap()
        requestDataMap.forEach {
            requestBodyMap[it.key] = it.value.toRequestBody()
        }
        return requestBodyMap
    }

    private fun getParamSign(map: MutableMap<String, String?>): String {
        val keys = map.keys.toMutableList()
        keys.sortBy { it }
        val sign = StringBuilder()
        for (key in keys) {
            if (map[key] != null && "${map[key]}" != "") {
                sign.append(key).append("=").append("${map[key]}").append("&")
            }
        }
        sign.append("59201CF6589202CB2CDAB26752472112")
        return ByteBufUtil.hexDump(
                MessageDigest.getInstance("MD5").digest(sign.toString().toByteArray())
        ).toUpperCase(Locale.getDefault())
    }

}