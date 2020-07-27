package com.shengshijie.servertest

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.shengshijie.dialog.external.InputDialog
import com.shengshijie.log.HLog
import com.shengshijie.servertest.api.State
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.text.SimpleDateFormat
import java.util.*

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel = MainViewModel()

    private var orderNumber: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        HLog.init(application, application.getExternalFilesDir(null)?.absolutePath, "RFT")
//        findViewById<View>(R.id.ll_content).scaleY = -1F
        mainViewModel.initResponseLiveData.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> {
                    tv_log.text = "${SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())} Loading"
                }
                is State.Success -> {
                    tv_log.text = "${SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())} 初始化成功"
                }
                is State.Error -> {
                    tv_log.text = "${SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())} 初始化失败:${state.message}"
                }
            }
        })
        mainViewModel.setAmountResponseLiveData.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> {
                    tv_log.text = "${SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())} Loading"
                }
                is State.Success -> {
                    tv_log.text = "${SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())} 设置金额成功"
                    orderNumber = state.data.data?.orderNumber ?: ""
                }
                is State.Error -> {
                    tv_log.text = "${SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())} 设置金额失败:${state.message}"
                }
            }
        })
        mainViewModel.startResponseLiveData.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> {
                    tv_log.text = "${SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())} Loading"
                }
                is State.Success -> {
                    tv_log.text = "${SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())} 交易成功:${state.data.data.toString()}"
                }
                is State.Error -> {
                    tv_log.text = "${SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())} 交易失败:${state.message}"
                }
            }
        })
        mainViewModel.changeAmountResponseLiveData.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> {
                    tv_log.text = "${SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())} Loading"
                }
                is State.Success -> {
                    tv_log.text = "${SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())} 修改金额成功"
                }
                is State.Error -> {
                    tv_log.text = "${SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())} 修改金额失败:${state.message}"
                }
            }
        })
        mainViewModel.setFaceResultResponseLiveData.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> {
                    tv_log.text = "${SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())} Loading"
                }
                is State.Success -> {
                    tv_log.text = "${SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())} 设置人脸信息成功"
                }
                is State.Error -> {
                    tv_log.text = "${SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())} 设置人脸信息失败:${state.message}"
                }
            }
        })
        mainViewModel.verifyPasswordResponseLiveData.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> {
                    tv_log.text = "${SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())} Loading"
                }
                is State.Success -> {
                    tv_log.text = "${SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())} 交易成功:${state.data.data.toString()}"
                }
                is State.Error -> {
                    tv_log.text = "${SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())} 交易失败:${state.message}"
                }
            }
        })
        mainViewModel.cancelResponseLiveData.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> {
                    tv_log.text = "${SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())} Loading"
                }
                is State.Success -> {
                    tv_log.text = "${SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())} 取消交易成功"
                }
                is State.Error -> {
                    tv_log.text = "${SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())} 取消交易失败:${state.message}"
                }
            }
        })
        mainViewModel.destroyResponseLiveData.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> {
                    tv_log.text = "${SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())} Loading"
                }
                is State.Success -> {
                    tv_log.text = "${SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())} 销毁交易成功"
                }
                is State.Error -> {
                    tv_log.text = "${SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())} 销毁交易失败:${state.message}"
                }
            }
        })
//        findViewById<View>(R.id.startServer).setOnClickListener {
//            ServerManager.start(ServerConfig.Builder()
//                    .setServer(AndroidServer(this@MainActivity))
//                    .setPort(8888)
//                    .setDebug(true)
//                    .setEnableSSL(false)
//                    .setEnableCors(true)
//                    .setSign(false)
//                    .setRootPath("/api")
//                    .setLog { level, content -> HLog.log(level.toAndroidLogLevel(), content) }
//                    .setLogLevel(LogLevel.INFO)
//                    .setPackageNameList(arrayListOf("com.shengshijie.servertest.controller"))
//                    .build())
//        }
//        findViewById<View>(R.id.stopServer).setOnClickListener {
//            ServerManager.stop()
//        }

    }

    fun init(view: View) {
        mainViewModel.init()
    }

    fun setAmount(view: View) {
        InputDialog.Builder(this)
                .setTitle("请输入支付金额")
                .setMessage("")
                .setInput("")
                .setOnClickPositive { dialog, text ->
                    if (text == null || text.isEmpty()) {
                        Toast.makeText(this, "请输入支付金额", Toast.LENGTH_SHORT).show()
                        return@setOnClickPositive
                    }
                    mainViewModel.setAmount(text)
                    dialog.dismiss()
                }
                .create()
                .show()
    }

    fun start(view: View) {
        mainViewModel.start(orderNumber)
    }

    fun setFaceResult(view: View) {
        mainViewModel.setFaceResult("左烨季", "1801562")
    }

    fun verifyPassword(view: View) {
        InputDialog.Builder(this)
                .setTitle("请输入支付密码")
                .setMessage("")
                .setInput("")
                .setOnClickPositive { dialog, text ->
                    if (text == null || text.isEmpty()) {
                        Toast.makeText(this, "请输入支付密码", Toast.LENGTH_SHORT).show()
                        return@setOnClickPositive
                    }
                    mainViewModel.verifyPassword(text)
                    dialog.dismiss()
                }
                .create()
                .show()
    }

    fun cancel(view: View) {
        mainViewModel.cancel()
    }

    fun destroy(view: View) {
        mainViewModel.destroy()
    }

    fun ip(view: View) {
        InputDialog.Builder(this)
                .setTitle("请输入POS终端IP")
                .setMessage("***.***.***.***")
                .setInput(SPHelper.ip)
                .setOnClickPositive { dialog, text ->
                    if (text != null) {
                        SPHelper.ip = text
                        dialog.dismiss()
                    }
                }
                .create()
                .show()
    }

    fun changeAmount(view: View) {
        mainViewModel.changeAmount("10.00")
    }

}