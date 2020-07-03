package com.shengshijie.servertest

import android.content.Context
import android.content.SharedPreferences
import com.shengshijie.sp.*

object SPHelper {

    private var sp: SharedPreferences = App.instance.getSharedPreferences("pay", Context.MODE_PRIVATE)

    var ip by sp.string()

}