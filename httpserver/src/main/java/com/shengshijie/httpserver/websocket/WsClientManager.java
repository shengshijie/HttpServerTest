package com.shengshijie.httpserver.websocket;

import com.google.gson.Gson;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public class WsClientManager {

    private static WsClientManager instance = new WsClientManager();

    private WsClientManager() {
    }

    public static WsClientManager getInstance() {
        return instance;
    }

    private Map<String, Channel> channelMap = new ConcurrentHashMap<>();

    public void putChannel(String id, Channel channel) {
        if(channelMap == null){
            channelMap = new ConcurrentHashMap<>();
        }
       channelMap.put(id, channel);
    }

    public void getChannel(String id) {
        channelMap.get(id);
    }

    public void removeChannel(String id) {
        channelMap.remove(id);
    }

    public void sendMsg(String id, String msg) {
        TextWebSocketFrame frame = new TextWebSocketFrame(msg);
        Channel channel = channelMap.get(id);
        if (channel != null) {
            channel.writeAndFlush(frame);
        }
    }

    public void sendMsg2All(String msg) {
        for (Channel channel : channelMap.values()) {
            TextWebSocketFrame frame = new TextWebSocketFrame(msg);
            channel.writeAndFlush(frame);
        }
    }

    public void handleMsg(String msgJson) {
        MsgVo msgVo = new Gson().fromJson(msgJson, MsgVo.class);
        if (msgVo.getType() == MsgTypeEnum.ONE2ONE.getCode()) {
            this.sendMsg(msgVo.getToId(), msgJson);
            this.sendMsg(msgVo.getFromId(), msgJson);
        } else if (msgVo.getType() == MsgTypeEnum.ONE2ALL.getCode()) {
            this.sendMsg2All(msgJson);
        }
    }

}