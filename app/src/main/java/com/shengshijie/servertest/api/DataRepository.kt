package com.shengshijie.servertest.api

import com.shengshijie.log.HLog
import com.shengshijie.servertest.ResponseUtils
import com.shengshijie.servertest.requset.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retry
import java.io.IOException
import kotlin.system.measureTimeMillis

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
        var orderNumber = ""
        repeat(500) {
            HLog.e("EEE", "︿︿︿︿︿︿︿︿︿︿︿︿︿︿︿︿")
            count++
            val initTime = measureTimeMillis {
                val init = RetrofitClient.getService().init(EmptyRequest())
                HLog.i("init:${init.code} ${init.message}")
            }
            val amountTime = measureTimeMillis {
                val amount = RetrofitClient.getService().setAmount(SetAmountRequest("0.01"))
                orderNumber = amount.data?.orderNumber ?: ""
                HLog.i("amount:${amount.code} ${amount.message}")
            }
            val startTime = measureTimeMillis {
                val start = RetrofitClient.getService().start(StartRequest(orderNumber, false))
                HLog.i("start:${start.code} ${start.message}")
                if (start.code != 1000) {
                    error++
                } else {
                    success++
                }
            }
            val orderTime = measureTimeMillis {
                val order = RetrofitClient.getService().order(QueryRequest(orderNumber))
                HLog.i("order:${order.code} ${order.message}")
            }
            HLog.e("EEE", "initTime:$initTime | amountTime:$amountTime | startTime:$startTime  | orderTime:$orderTime")
            HLog.e("EEE", "﹀﹀﹀﹀﹀﹀﹀﹀﹀﹀﹀﹀﹀﹀﹀﹀  TOTAL:$count | SUCCESS:$success | ERROR:$error")
        }
        HLog.e("EEE", "TOTAL:$count | SUCCESS:$success | ERROR:$error")
    }

    suspend fun test2() {
        var count = 0
        var success = 0
        var error = 0
        var orderNumber = ""
        repeat(500) {
            HLog.e("EEE", "︿︿︿︿︿︿︿︿︿︿︿︿︿︿︿︿")
            count++
            val initTime = measureTimeMillis {
                val init = RetrofitClient.getService().init(EmptyRequest())
                HLog.i("init:${init.code} ${init.message}")
            }
            val amountTime = measureTimeMillis {
                val amount = RetrofitClient.getService().setAmount(SetAmountRequest("0.01"))
                orderNumber = amount.data?.orderNumber ?: ""
                HLog.i("amount:${amount.code} ${amount.message}")
            }
            val startTime = measureTimeMillis {
                val start = RetrofitClient.getService().start(StartRequest(orderNumber, true))
                HLog.i("start:${start.code} ${start.message}")
                var startSuccess = false
                val change = RetrofitClient.getService().changeAmount(ChangeAmountRequest("0.02"))
                HLog.i("change:${change.code} ${change.message}")
                var retry = 0
                do {
                    if (retry > 5) {
                        break
                    }
                    val order = RetrofitClient.getService().query(QueryRequest(orderNumber))
                    HLog.i("order:${order.code} ${if (order.code == 1000) "交易成功!!!" else order.message}")
                    val change2 = RetrofitClient.getService().changeAmount(ChangeAmountRequest("0.02"))
                    HLog.i("change:${change2.code} ${change2.message}")
                    retry++
                    delay(500)
                    startSuccess = order.code == 1000
                } while (!startSuccess)
                if (startSuccess) {
                    success++
                } else {
                    error++
                }
            }
            HLog.e("EEE", "initTime:$initTime | amountTime:$amountTime | startTime:$startTime")
            HLog.e("EEE", "﹀﹀﹀﹀﹀﹀﹀﹀﹀﹀﹀﹀﹀﹀﹀﹀  TOTAL:$count | SUCCESS:$success | ERROR:$error")
        }
        HLog.e("EEE", "TOTAL:$count | SUCCESS:$success | ERROR:$error")
    }

    suspend fun test3(faceBase64: String, userName: String, userNumber: String, similarity: String, threshold: String, captureTime: String) {
        repeat(1000) {
            RetrofitClient.getService().setFaceResult(SetFaceResultRequest(faceBase64, userName, userNumber, similarity, threshold, captureTime))
            delay(1000)
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

