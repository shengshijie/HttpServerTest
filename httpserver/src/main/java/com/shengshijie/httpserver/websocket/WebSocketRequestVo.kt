package com.shengshijie.httpserver.websocket

import io.netty.handler.codec.http.FullHttpRequest
import io.netty.handler.codec.http.websocketx.WebSocketFrame

class WebSocketRequestVo {
    var request: FullHttpRequest? = null
    var frame: WebSocketFrame? = null

    constructor(request: FullHttpRequest?) {
        this.request = request
    }

    constructor(frame: WebSocketFrame?) {
        this.frame = frame
    }

}