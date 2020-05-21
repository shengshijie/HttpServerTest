package com.shengshijie.server.http.exception

import com.shengshijie.server.http.config.Constant

class BusinessException @JvmOverloads constructor(msg: String, val code: Int = Constant.ERROR_CODE_BUSINESS) : Exception(msg)