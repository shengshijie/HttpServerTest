package com.shengshijie.server.http

import io.netty.channel.Channel

object ChannelHolder {

    private val channelHolder = ThreadLocal<Channel>()

    fun set(channel: Channel) {
        channelHolder.set(channel)
    }

    fun unset() {
        channelHolder.remove()
    }

    fun get(): Channel? {
        return channelHolder.get()
    }
}