package com.shengshijie.httpservertest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shengshijie.httpservertest.api.*
import com.shengshijie.httpservertest.controller.requset.SetAmountRequest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class MainViewModel : ViewModel() {

    private val _initResponseLiveData = MutableLiveData<State<BaseResponse>>()
    val initResponseLiveData: LiveData<State<BaseResponse>>
        get() = _initResponseLiveData

    private val _setAmountResponseLiveData = MutableLiveData<State<SetAmountResponse>>()
    val setAmountResponseLiveData: LiveData<State<SetAmountResponse>>
        get() = _setAmountResponseLiveData

    private val _startResponseLiveData = MutableLiveData<State<BaseResponse>>()
    val startResponseLiveData: LiveData<State<BaseResponse>>
        get() = _startResponseLiveData

    private val _verifyPasswordResponseLiveData = MutableLiveData<State<BaseResponse>>()
    val verifyPasswordResponseLiveData: LiveData<State<BaseResponse>>
        get() = _verifyPasswordResponseLiveData

    private val _cancelResponseLiveData = MutableLiveData<State<BaseResponse>>()
    val cancelResponseLiveData: LiveData<State<BaseResponse>>
        get() = _cancelResponseLiveData

    private val _destroyResponseLiveData = MutableLiveData<State<BaseResponse>>()
    val destroyResponseLiveData: LiveData<State<BaseResponse>>
        get() = _destroyResponseLiveData

    fun init() {
        viewModelScope.launch {
            DataRepository(RetrofitClient.getService()).init().collect {
                _initResponseLiveData.value = it
            }
        }
    }

    fun setAmount(amount: Double) {
        viewModelScope.launch {
            DataRepository(RetrofitClient.getService()).setAmount(amount).collect {
                _setAmountResponseLiveData.value = it
            }
        }
    }

    fun start() {
        viewModelScope.launch {
            DataRepository(RetrofitClient.getService()).start().collect {
                _startResponseLiveData.value = it
            }
        }
    }

    fun verifyPassword(password: String) {
        viewModelScope.launch {
            DataRepository(RetrofitClient.getService()).verifyPassword(password).collect {
                _verifyPasswordResponseLiveData.value = it
            }
        }
    }

    fun cancel() {
        viewModelScope.launch {
            DataRepository(RetrofitClient.getService()).cancel().collect {
                _cancelResponseLiveData.value = it
            }
        }
    }

    fun destroy() {
        viewModelScope.launch {
            DataRepository(RetrofitClient.getService()).destroy().collect {
                _destroyResponseLiveData.value = it
            }
        }
    }

}