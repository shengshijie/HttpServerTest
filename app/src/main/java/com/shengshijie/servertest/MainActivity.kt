package com.shengshijie.servertest

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.shengshijie.log.HLog
import com.shengshijie.log.LogbackImpl
import com.shengshijie.server.ServerManager
import com.shengshijie.server.http.config.ServerConfig
import com.shengshijie.server.log.LogLevel
import com.shengshijie.server.platform.android.AndroidServer
import com.shengshijie.servertest.api.State
import com.shengshijie.servertest.java.TestActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel = MainViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        HLog.setLogImpl(LogbackImpl().apply {
            file = true
            db = true
            socket = true
            socketHost = "192.168.88.191"
            socketPort = 4569
        })
        HLog.init(application, application.getExternalFilesDir(null)?.absolutePath, "RFT")
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
                    .setServer(AndroidServer(this@MainActivity))
                    .setDebug(true)
                    .setLog { level, content -> HLog.log(level.toAndroidLogLevel(), content) }
                    .setLogLevel(LogLevel.INFO)
                    .setPackageNameList(arrayListOf("com.shengshijie.servertest.controller"))
                    .build())
        }
        findViewById<View>(R.id.stopServer).setOnClickListener {
            ServerManager.stop()
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