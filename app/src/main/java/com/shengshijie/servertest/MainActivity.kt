package com.shengshijie.servertest

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.shengshijie.dialog.external.InputDialog
import com.shengshijie.dialog.external.RadioDialog
import com.shengshijie.log.HLog
import com.shengshijie.servertest.api.State
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    private var orderNumber: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        HLog.init(application, application.getExternalFilesDir(null)?.absolutePath, "RFT")
//        findViewById<View>(R.id.ll_content).scaleY = -1F
        mainViewModel.initResponseLiveData.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> {
                    tv_log.text = text("Loading")
                }
                is State.Success -> {
                    tv_log.text = text("初始化成功:${state.data}")
                }
                is State.Error -> {
                    tv_log.text = text("初始化失败:${state.message}")
                }
            }
        })
        mainViewModel.setAmountResponseLiveData.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> {
                    tv_log.text = text("Loading")
                }
                is State.Success -> {
                    tv_log.text = text("设置金额成功:${state.data}")
                    orderNumber = state.data.orderNumber
                }
                is State.Error -> {
                    tv_log.text = text("设置金额失败:${state.message}")
                }
            }
        })
        mainViewModel.startResponseLiveData.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> {
                    tv_log.text = text("Loading")
                }
                is State.Success -> {
                    tv_log.text = text("交易成功:${state.data}")
                }
                is State.Error -> {
                    tv_log.text = text("交易失败:${state.message}")
                }
            }
        })
        mainViewModel.changeAmountResponseLiveData.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> {
                    tv_log.text = text("Loading")
                }
                is State.Success -> {
                    tv_log.text = text("修改金额成功:${state.data}")
                }
                is State.Error -> {
                    tv_log.text = text("修改金额失败:${state.message}")
                }
            }
        })
        mainViewModel.setFaceResultResponseLiveData.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> {
                    tv_log.text = text("Loading")
                }
                is State.Success -> {
                    tv_log.text = text("设置人脸信息成功:${state.data}")
                }
                is State.Error -> {
                    tv_log.text = text("设置人脸信息失败:${state.message}")
                }
            }
        })
        mainViewModel.verifyPasswordResponseLiveData.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> {
                    tv_log.text = text("Loading")
                }
                is State.Success -> {
                    tv_log.text = text("交易成功:${state.data}")
                }
                is State.Error -> {
                    tv_log.text = text("交易失败:${state.message}")
                }
            }
        })
        mainViewModel.cancelResponseLiveData.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> {
                    tv_log.text = text("Loading")
                }
                is State.Success -> {
                    tv_log.text = text("取消交易成功:${state.data}")
                }
                is State.Error -> {
                    tv_log.text = text("取消交易失败:${state.message}")
                }
            }
        })
        mainViewModel.destroyResponseLiveData.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> {
                    tv_log.text = text("Loading")
                }
                is State.Success -> {
                    tv_log.text = text("销毁交易成功:${state.data}")
                }
                is State.Error -> {
                    tv_log.text = text("销毁交易失败:${state.message}")
                }
            }
        })
        mainViewModel.orderResponseLiveData.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> {
                    tv_log.text = text("Loading")
                }
                is State.Success -> {
                    tv_log.text = text("查询交易成功:${state.data}")
                }
                is State.Error -> {
                    tv_log.text = text("查询交易失败:${state.message}")
                }
            }
        })
        mainViewModel.queryResponseLiveData.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> {
                    tv_log.text = text("Loading")
                }
                is State.Success -> {
                    tv_log.text = text("轮询交易成功:${state.data}")
                }
                is State.Error -> {
                    tv_log.text = text("轮询交易失败:${state.message}")
                }
            }
        })
        mainViewModel.detailResponseLiveData.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> {
                    tv_log.text = text("Loading")
                }
                is State.Success -> {
                    tv_log.text = text("查询个人信息成功:${state.data}")
                }
                is State.Error -> {
                    tv_log.text = text("查询个人信息失败:${state.message}")
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

    private fun text(content: String) = "${SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(Date())} $content "

    fun init(view: View) {
        mainViewModel.init()
    }

    fun setAmount(view: View) {
        InputDialog.Builder(this)
                .setTitle("请输入支付金额")
                .setMessage("")
                .setInput("2")
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
        RadioDialog.Builder(this)
                .setTitle("请输入是否立即返回")
                .setItems(arrayOf("是","否"))
                .setOnClickPositive { dialog, index ->
                    mainViewModel.start(orderNumber, 0 == index)
                    dialog.dismiss()
                }
                .create()
                .show()
    }

    fun setFaceResult(view: View) {
        mainViewModel.setFaceResult("", "左烨季", "1801562", "0.8", "80", "${System.currentTimeMillis()}")
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

    fun order(view: View) {
        mainViewModel.order(orderNumber)
    }

    fun query(view: View) {
        mainViewModel.query(orderNumber)
    }

    fun detail(view: View) {
        mainViewModel.detail()
    }

    fun test1(view: View) {
        mainViewModel.test1()
    }

    fun test2(view: View) {
        mainViewModel.test2()
    }

}