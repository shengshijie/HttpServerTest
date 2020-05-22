package com.shengshijie.server.http.exception

import com.shengshijie.server.ServerManager

class BusinessException @JvmOverloads constructor(msg: String, val code: Int = ServerManager.mServerConfig.errorCode) : Exception(msg)