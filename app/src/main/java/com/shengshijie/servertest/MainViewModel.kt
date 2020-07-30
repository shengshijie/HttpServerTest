package com.shengshijie.servertest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shengshijie.servertest.api.*
import com.shengshijie.servertest.response.BaseResponse
import com.shengshijie.servertest.response.PayResultResponse
import com.shengshijie.servertest.response.QueryResponse
import com.shengshijie.servertest.response.SetAmountResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class MainViewModel : ViewModel() {

    private val _initResponseLiveData = MutableLiveData<State<BaseResponse<Unit>>>()
    val initResponseLiveData: LiveData<State<BaseResponse<Unit>>>
        get() = _initResponseLiveData

    private val _setAmountResponseLiveData = MutableLiveData<State<BaseResponse<SetAmountResponse>>>()
    val setAmountResponseLiveData: LiveData<State<BaseResponse<SetAmountResponse>>>
        get() = _setAmountResponseLiveData

    private val _startResponseLiveData = MutableLiveData<State<BaseResponse<PayResultResponse>>>()
    val startResponseLiveData: LiveData<State<BaseResponse<PayResultResponse>>>
        get() = _startResponseLiveData

    private val _changeAmountResponseLiveData = MutableLiveData<State<BaseResponse<Unit>>>()
    val changeAmountResponseLiveData: LiveData<State<BaseResponse<Unit>>>
        get() = _changeAmountResponseLiveData

    private val _setFaceResultResponseLiveData = MutableLiveData<State<BaseResponse<Unit>>>()
    val setFaceResultResponseLiveData: LiveData<State<BaseResponse<Unit>>>
        get() = _setFaceResultResponseLiveData

    private val _verifyPasswordResponseLiveData = MutableLiveData<State<BaseResponse<PayResultResponse>>>()
    val verifyPasswordResponseLiveData: LiveData<State<BaseResponse<PayResultResponse>>>
        get() = _verifyPasswordResponseLiveData

    private val _cancelResponseLiveData = MutableLiveData<State<BaseResponse<Unit>>>()
    val cancelResponseLiveData: LiveData<State<BaseResponse<Unit>>>
        get() = _cancelResponseLiveData

    private val _destroyResponseLiveData = MutableLiveData<State<BaseResponse<Unit>>>()
    val destroyResponseLiveData: LiveData<State<BaseResponse<Unit>>>
        get() = _destroyResponseLiveData

    private val _orderResponseLiveData = MutableLiveData<State<BaseResponse<List<QueryResponse>>>>()
    val orderResponseLiveData: LiveData<State<BaseResponse<List<QueryResponse>>>>
        get() = _orderResponseLiveData

    fun init() {
        viewModelScope.launch {
            DataRepository().init().collect {
                _initResponseLiveData.value = it
            }
        }
    }

    fun setAmount(amount: String) {
        viewModelScope.launch {
            DataRepository().setAmount(amount).collect {
                _setAmountResponseLiveData.value = it
            }
        }
    }

    fun start(orderNumber: String) {
        viewModelScope.launch {
            DataRepository().start(orderNumber).collect {
                _startResponseLiveData.value = it
            }
        }
    }

    fun changeAmount(amount: String) {
        viewModelScope.launch {
            DataRepository().changeAmount(amount).collect {
                _changeAmountResponseLiveData.value = it
            }
        }
    }


    fun setFaceResult(userName: String, userNumber: String) {
        viewModelScope.launch {
            DataRepository().setFaceResult(userName, userNumber).collect {
                _setFaceResultResponseLiveData.value = it
            }
        }
    }

    fun verifyPassword(password: String) {
        viewModelScope.launch {
            DataRepository().verifyPassword(password).collect {
                _verifyPasswordResponseLiveData.value = it
            }
        }
    }

    fun cancel() {
        viewModelScope.launch {
            DataRepository().cancel().collect {
                _cancelResponseLiveData.value = it
            }
        }
    }

    fun destroy() {
        viewModelScope.launch {
            DataRepository().destroy().collect {
                _destroyResponseLiveData.value = it
            }
        }
    }

    fun order(orderNumber: String?) {
        viewModelScope.launch {
            DataRepository().order(orderNumber).collect {
                _orderResponseLiveData.value = it
            }
        }
    }


}