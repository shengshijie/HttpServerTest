package com.shengshijie.httpservertest

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.shengshijie.httpservertest.api.State
import com.shengshijie.log.HLog
import com.shengshijie.server.ServerManager
import com.shengshijie.server.http.config.Config
import com.shengshijie.server.platform.AndroidServer
import io.netty.handler.logging.LogLevel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel = MainViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        HLog.init(this, "")
        mainViewModel.initResponseLiveData.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> {
                    tv_log.text = "Loading"
                }
                is State.Success -> {
                    tv_log.text = state.data.toString()
                }
                is State.Error -> {
                    tv_log.text = state.message
                }
            }
        })
        mainViewModel.setAmountResponseLiveData.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> {
                    tv_log.text = "Loading"
                }
                is State.Success -> {
                    tv_log.text = state.data.toString()
                }
                is State.Error -> {
                    tv_log.text = state.message
                }
            }
        })
        mainViewModel.startResponseLiveData.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> {
                    tv_log.text = "Loading"
                }
                is State.Success -> {
                    tv_log.text = state.data.toString()
                }
                is State.Error -> {
                    tv_log.text = state.message
                }
            }
        })
        mainViewModel.verifyPasswordResponseLiveData.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> {
                    tv_log.text = "Loading"
                }
                is State.Success -> {
                    tv_log.text = state.data.toString()
                }
                is State.Error -> {
                    tv_log.text = state.message
                }
            }
        })
        mainViewModel.cancelResponseLiveData.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> {
                    tv_log.text = "Loading"
                }
                is State.Success -> {
                    tv_log.text = state.data.toString()
                }
                is State.Error -> {
                    tv_log.text = state.message
                }
            }
        })
        mainViewModel.destroyResponseLiveData.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> {
                    tv_log.text = "Loading"
                }
                is State.Success -> {
                    tv_log.text = state.data.toString()
                }
                is State.Error -> {
                    tv_log.text = state.message
                }
            }
        })
        findViewById<View>(R.id.startServer).setOnClickListener {
            ServerManager.start(Config.apply {
                server = AndroidServer.setContext(this@MainActivity)
                port = 8888
                debug =true
                logLevel = LogLevel.INFO
                packageNameList = arrayListOf("com.shengshijie.httpservertest.controller")
            })
        }
        findViewById<View>(R.id.stopServer).setOnClickListener {
            ServerManager.stop()
        }
        findViewById<View>(R.id.init).setOnClickListener {
            mainViewModel.init()
        }
        findViewById<View>(R.id.setAmount).setOnClickListener {
            mainViewModel.setAmount(0.01)
        }
        findViewById<View>(R.id.start).setOnClickListener {
            mainViewModel.start()
        }
        findViewById<View>(R.id.verifyPassword).setOnClickListener {
            mainViewModel.verifyPassword("123456")
        }
        findViewById<View>(R.id.cancel).setOnClickListener {
            mainViewModel.cancel()
        }
        findViewById<View>(R.id.destroy).setOnClickListener {
            mainViewModel.destroy()
        }
    }
}