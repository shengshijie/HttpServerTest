package com.shengshijie.httpserver.websocket

enum class MsgTypeEnum(var code: Int, var cnName: String) {

    ONE2ONE(1, "私聊"),
    ONE2ALL(2, "公屏");

}