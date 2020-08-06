package com.shengshijie.servertest

import com.shengshijie.servertest.api.State
import com.shengshijie.servertest.response.BaseResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

object ResponseUtils {

    suspend inline fun <T> createFlow(crossinline request: suspend () -> BaseResponse<T>): Flow<State<T>> {
        return flow {
            emit(State.loading<T>())
            val response = request()
            if (response.code == 1000) {
                emit(State.success(response.data as T))
            } else {
                emit(State.error(response.message))
            }
        }.flowOn(Dispatchers.IO)
                .catch {
                    emit(State.error("Network error:" + it.message))
                }
    }

}