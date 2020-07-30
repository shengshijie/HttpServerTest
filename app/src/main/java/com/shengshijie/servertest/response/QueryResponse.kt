package com.shengshijie.servertest.response

data class QueryResponse(

    var orderNumber: String? = null,
    var orderAmount: String? = null,
    var orderTime: String? = null,
    var orderType: String? = null,
    var orderStatus: String? = null,
    var userNumber: String? = null

)