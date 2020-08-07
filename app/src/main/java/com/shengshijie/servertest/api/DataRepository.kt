package com.shengshijie.servertest.api

import android.util.Log
import com.shengshijie.log.HLog
import com.shengshijie.servertest.ResponseUtils
import com.shengshijie.servertest.requset.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

object DataRepository {

    suspend fun init() = ResponseUtils.createFlow {
        RetrofitClient.getService().init(EmptyRequest())
    }

    suspend fun setAmount(amount: String) = ResponseUtils.createFlow {
        RetrofitClient.getService().setAmount(SetAmountRequest(amount))
    }

    suspend fun start(orderNumber: String, instant: Boolean) = ResponseUtils.createFlow {
        RetrofitClient.getService().start(StartRequest(orderNumber, instant))
    }

    suspend fun changeAmount(amount: String) = ResponseUtils.createFlow {
        RetrofitClient.getService().changeAmount(ChangeAmountRequest(amount))
    }

    suspend fun setFaceResult(faceBase64: String, userName: String, userNumber: String, similarity: String, threshold: String, captureTime: String) = ResponseUtils.createFlow {
        RetrofitClient.getService().setFaceResult(SetFaceResultRequest(faceBase64, userName, userNumber, similarity, threshold, captureTime))
    }

    suspend fun verifyPassword(password: String) = ResponseUtils.createFlow {
        RetrofitClient.getService().verifyPassword(PasswordRequest(password))
    }

    suspend fun cancel() = ResponseUtils.createFlow {
        RetrofitClient.getService().cancel(EmptyRequest())
    }

    suspend fun destroy() = ResponseUtils.createFlow {
        RetrofitClient.getService().destroy(EmptyRequest())
    }

    suspend fun order(orderNumber: String?) = ResponseUtils.createFlow {
        RetrofitClient.getService().order(QueryRequest(orderNumber))
    }

    suspend fun query(orderNumber: String?) = ResponseUtils.createFlow {
        RetrofitClient.getService().query(QueryRequest(orderNumber))
    }

    suspend fun detail() = ResponseUtils.createFlow {
        RetrofitClient.getService().detail(EmptyRequest())
    }

    suspend fun test1() {
        var count = 0
        var success = 0
        var error = 0
        repeat(10) {
            count++
            val init = RetrofitClient.getService().init(EmptyRequest())
            if (init.code != 1000 && init.message != "请勿重复初始化") {
                println("init:${init.message}")
            }
            val amount = RetrofitClient.getService().setAmount(SetAmountRequest("1.00"))
            if (init.code != 1000) {
                println("amount:${amount.message}")
            }
            val start = RetrofitClient.getService().start(StartRequest(amount.data?.orderNumber ?: "", false))
            if (start.code != 1000) {
                error++
                println("start:${start.message}")
            } else {
                success++
            }
            val order = RetrofitClient.getService().order(QueryRequest(amount.data?.orderNumber ?: ""))
            if (order.code != 1000) {
                println("order:${order.message}")
            }
            val cancel = RetrofitClient.getService().cancel(EmptyRequest())
            if (cancel.code != 1000) {
                println("cancel:${cancel.message}")
            }
            val detail = RetrofitClient.getService().detail(EmptyRequest())
            if (detail.code != 1000) {
                println("detail:${detail.message}")
            }
            delay(1000)
            Log.e("EEE", "TOTAL:$count | SUCCESS:$success | ERROR:$error")
        }
    }

    suspend fun test2() {
        var count = 0
        var success = 0
        var error = 0
        repeat(10) {
            count++
            val init = RetrofitClient.getService().init(EmptyRequest())
            if (init.code != 1000 && init.message != "请勿重复初始化") {
                println("init:${init.message}")
            }
            val amount = RetrofitClient.getService().setAmount(SetAmountRequest("1.00"))
            if (init.code != 1000) {
                println("amount:${amount.message}")
            }
            val start = RetrofitClient.getService().start(StartRequest(amount.data?.orderNumber ?: "", true))
            if (start.code != 1000) {
                println("start:${start.message}")
            }
            var startSuccess = false
            var retry = 0
            do {
                if (retry > 5) {
                    break
                }
                val order = RetrofitClient.getService().query(QueryRequest(amount.data?.orderNumber ?: ""))
                retry++
                delay(1000)
                startSuccess = order.code == 1000
            } while (!startSuccess)
            if (startSuccess) {
                success++
            } else {
                error++
            }
            delay(1000)
            Log.e("EEE", "TOTAL:$count | SUCCESS:$success | ERROR:$error")
        }
    }

    suspend fun test() = flow { emit(RetrofitClient.getService().init(EmptyRequest())) }
            .flatMapConcat {
                if (it.code == 1000) {
                    flow { emit(RetrofitClient.getService().setAmount(SetAmountRequest("1.00"))) }
                } else {
                    throw RuntimeException(it.message)
                }
            }
            .flatMapConcat {
                if (it.code == 1000) {
                    flow { emit(RetrofitClient.getService().start(StartRequest(it.data?.orderNumber ?: "", true))) }
                } else {
                    throw RuntimeException(it.message)
                }
            }
            .flatMapConcat {
                flow { emit(RetrofitClient.getService().query(QueryRequest(it.data?.orderNumber ?: ""))) }
                        .retry(retries = 3) { cause ->
                            if (cause is IOException) {
                                delay(1000)
                                return@retry true
                            } else {
                                return@retry false
                            }
                        }.catch {
                            throw RuntimeException(it.message)
                        }
            }

}

