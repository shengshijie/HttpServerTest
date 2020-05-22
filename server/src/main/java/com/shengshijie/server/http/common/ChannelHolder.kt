package com.shengshijie.server.http.common

import io.netty.channel.Channel

internal object ChannelHolder {

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