package com.shengshijie.httpserver.websocket

import com.google.gson.Gson
import io.netty.channel.Channel
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame
import java.util.concurrent.ConcurrentHashMap

object WsClientManager{

    private var channelMap: MutableMap<String?, Channel>? = ConcurrentHashMap()
    fun putChannel(id: String?, channel: Channel) {
        if (channelMap == null) {
            channelMap = ConcurrentHashMap()
        }
        channelMap!![id] = channel
    }

    fun getChannel(id: String?) {
        channelMap!![id]
    }

    fun removeChannel(id: String?) {
        channelMap!!.remove(id)
    }

    fun sendMsg(id: String?, msg: String?) {
        val frame = TextWebSocketFrame(msg)
        val channel = channelMap!![id]
        channel?.writeAndFlush(frame)
    }

    fun sendMsg2All(msg: String?) {
        for (channel in channelMap!!.values) {
            val frame = TextWebSocketFrame(msg)
            channel.writeAndFlush(frame)
        }
    }

    fun handleMsg(msgJson: String?) {
        val msgVo = Gson().fromJson(msgJson, MsgVo::class.java)
        if (msgVo.type == MsgTypeEnum.ONE2ONE.code) {
            sendMsg(msgVo.toId, msgJson)
            sendMsg(msgVo.fromId, msgJson)
        } else if (msgVo.type == MsgTypeEnum.ONE2ALL.code) {
            sendMsg2All(msgJson)
        }
    }

}