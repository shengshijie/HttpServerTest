package com.shengshijie.servertest.response

data class PayResultResponse(

    var verifyPassword: Boolean?,

    var orderNumber: String?,
    var payTypeName: String?,

    var orderAmount: String?,
    var receivableAmount: String?,
    var payableAmount: String?,
    var discountAmount: String?,
    var subsidyAmount: String?,

    var balance: String?,
    var userNumber: String?,
    var userName: String?

)