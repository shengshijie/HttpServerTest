package com.shengshijie.servertest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shengshijie.log.HLog
import com.shengshijie.servertest.api.*
import com.shengshijie.servertest.response.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _initResponseLiveData = MutableLiveData<State<Unit>>()
    val initResponseLiveData: LiveData<State<Unit>> = _initResponseLiveData

    private val _setAmountResponseLiveData = MutableLiveData<State<SetAmountResponse>>()
    val setAmountResponseLiveData: LiveData<State<SetAmountResponse>> = _setAmountResponseLiveData

    private val _startResponseLiveData = MutableLiveData<State<PayResultResponse>>()
    val startResponseLiveData: LiveData<State<PayResultResponse>> = _startResponseLiveData

    private val _changeAmountResponseLiveData = MutableLiveData<State<Unit>>()
    val changeAmountResponseLiveData: LiveData<State<Unit>> = _changeAmountResponseLiveData

    private val _setFaceResultResponseLiveData = MutableLiveData<State<Unit>>()
    val setFaceResultResponseLiveData: LiveData<State<Unit>> = _setFaceResultResponseLiveData

    private val _verifyPasswordResponseLiveData = MutableLiveData<State<PayResultResponse>>()
    val verifyPasswordResponseLiveData: LiveData<State<PayResultResponse>> = _verifyPasswordResponseLiveData

    private val _cancelResponseLiveData = MutableLiveData<State<Unit>>()
    val cancelResponseLiveData: LiveData<State<Unit>> = _cancelResponseLiveData

    private val _destroyResponseLiveData = MutableLiveData<State<Unit>>()
    val destroyResponseLiveData: LiveData<State<Unit>> = _destroyResponseLiveData

    private val _orderResponseLiveData = MutableLiveData<State<QueryResponse>>()
    val orderResponseLiveData: LiveData<State<QueryResponse>> = _orderResponseLiveData

    private val _queryResponseLiveData = MutableLiveData<State<PayResultResponse>>()
    val queryResponseLiveData: LiveData<State<PayResultResponse>> = _queryResponseLiveData

    private val _detailResponseLiveData = MutableLiveData<State<PersonResponse>>()
    val detailResponseLiveData: LiveData<State<PersonResponse>> = _detailResponseLiveData

    fun init() {
        viewModelScope.launch {
            DataRepository.init().collect {
                _initResponseLiveData.value = it
            }
        }
    }

    fun setAmount(amount: String) {
        viewModelScope.launch {
            DataRepository.setAmount(amount).collect {
                _setAmountResponseLiveData.value = it
            }
        }
    }

    fun start(orderNumber: String, instant: Boolean) {
        viewModelScope.launch {
            DataRepository.start(orderNumber, instant).collect {
                _startResponseLiveData.value = it
            }
        }
    }

    fun changeAmount(amount: String) {
        viewModelScope.launch {
            DataRepository.changeAmount(amount).collect {
                _changeAmountResponseLiveData.value = it
            }
        }
    }


    fun setFaceResult(faceBase64: String, userName: String, userNumber: String, similarity: String, threshold: String, captureTime: String) {
        viewModelScope.launch {
            DataRepository.setFaceResult(faceBase64, userName, userNumber, similarity, threshold, captureTime).collect {
                _setFaceResultResponseLiveData.value = it
            }
        }
    }

    fun verifyPassword(password: String) {
        viewModelScope.launch {
            DataRepository.verifyPassword(password).collect {
                _verifyPasswordResponseLiveData.value = it
            }
        }
    }

    fun cancel() {
        viewModelScope.launch {
            DataRepository.cancel().collect {
                _cancelResponseLiveData.value = it
            }
        }
    }

    fun destroy() {
        viewModelScope.launch {
            DataRepository.destroy().collect {
                _destroyResponseLiveData.value = it
            }
        }
    }

    fun order(orderNumber: String?) {
        viewModelScope.launch {
            DataRepository.order(orderNumber).collect {
                _orderResponseLiveData.value = it
            }
        }
    }

    fun query(orderNumber: String?) {
        viewModelScope.launch {
            DataRepository.query(orderNumber).collect {
                _queryResponseLiveData.value = it
            }
        }
    }

    fun detail() {
        viewModelScope.launch {
            DataRepository.detail().collect {
                _detailResponseLiveData.value = it
            }
        }
    }

    fun test1() {
        viewModelScope.launch {
            DataRepository.test1()
        }
    }

    fun test2() {
        viewModelScope.launch {
            DataRepository.test2()
        }
    }

}