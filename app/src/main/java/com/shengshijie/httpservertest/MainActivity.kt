package com.shengshijie.httpservertest

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.shengshijie.httpservertest.api.State
import com.shengshijie.httpservertest.java.TestActivity
import com.shengshijie.log.HLog
import com.shengshijie.server.ServerManager
import com.shengshijie.server.http.config.ServerConfig
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
            ServerManager.start(ServerConfig.Builder()
                    .setPort(8888)
                    .setServer(AndroidServer().setContext(this@MainActivity))
                    .setDebug(true)
                    .setLogLevel(LogLevel.INFO)
                    .setPackageNameList(arrayListOf("com.shengshijie.httpservertest.controller"))
                    .build()) { result -> HLog.e(result.toString()) }
        }
        findViewById<View>(R.id.stopServer).setOnClickListener {
            ServerManager.stop() { result -> HLog.e(result.toString()) }
        }
        findViewById<View>(R.id.init).setOnClickListener {
            mainViewModel.init()
        }
        findViewById<View>(R.id.setAmount).setOnClickListener {
            mainViewModel.setAmount(1.00)
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
        findViewById<View>(R.id.java).setOnClickListener {
            startActivity(Intent(this, TestActivity::class.java))
        }
    }
}