package com.shengshijie.servertest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shengshijie.servertest.api.*
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

    fun get2() {
        viewModelScope.launch {
            DataRepository(RetrofitClient.getService()).get2().collect {
                _initResponseLiveData.value = it
            }
        }
    }

    fun get1(amount: Double) {
        viewModelScope.launch {
            DataRepository(RetrofitClient.getService()).get1(amount).collect {
                _setAmountResponseLiveData.value = it
            }
        }
    }

    fun post2() {
        viewModelScope.launch {
            DataRepository(RetrofitClient.getService()).post2().collect {
                _startResponseLiveData.value = it
            }
        }
    }

    fun post1(name: String,age: String,amount: String) {
        viewModelScope.launch {
            DataRepository(RetrofitClient.getService()).post1(name, age, amount).collect {
                _verifyPasswordResponseLiveData.value = it
            }
        }
    }

}