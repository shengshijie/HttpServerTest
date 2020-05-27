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
            file = false
            db = false
            socket = false
            socketHost = "192.168.88.191"
            socketPort = 4569
        })
        HLog.init(application, application.getExternalFilesDir(null)?.absolutePath, "RFT")
//        findViewById<View>(R.id.ll_content).scaleY = -1F
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
                    .setServer(AndroidServer(this@MainActivity))
                    .setPort(8888)
                    .setDebug(true)
                    .setEnableSSL(false)
                    .setEnableCors(true)
                    .setSign(false)
                    .setRootPath("/api")
                    .setLog { level, content -> HLog.log(level.toAndroidLogLevel(), content) }
                    .setLogLevel(LogLevel.INFO)
                    .setPackageNameList(arrayListOf("com.shengshijie.servertest.controller"))
                    .build())
        }
        findViewById<View>(R.id.stopServer).setOnClickListener {
            ServerManager.stop()
        }
        findViewById<View>(R.id.get2).setOnClickListener {
            mainViewModel.get2()
        }
        findViewById<View>(R.id.get1).setOnClickListener {
            mainViewModel.get1(1.00)
        }
        findViewById<View>(R.id.post2).setOnClickListener {
            mainViewModel.post2()
        }
        findViewById<View>(R.id.post1).setOnClickListener {
            mainViewModel.post1("西西", "10", "1.00")
        }
        findViewById<View>(R.id.java).setOnClickListener {
            startActivity(Intent(this, TestActivity::class.java))
        }
    }
}