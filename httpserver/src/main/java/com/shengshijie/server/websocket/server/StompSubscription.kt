package com.shengshijie.server.websocket.server

import io.netty.channel.Channel
import io.netty.util.internal.ObjectUtil

class StompSubscription(id: String, destination: String, channel: Channel) {
    private val id: String
    private val destination: String
    private val channel: Channel
    fun id(): String {
        return id
    }

    fun destination(): String {
        return destination
    }

    fun channel(): Channel {
        return channel
    }

    override fun equals(obj: Any?): Boolean {
        if (this === obj) {
            return true
        }
        if (obj == null || javaClass != obj.javaClass) {
            return false
        }
        val that = obj as StompSubscription
        if (id != that.id) {
            return false
        }
        return if (destination != that.destination) {
            false
        } else channel == that.channel
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + destination.hashCode()
        result = 31 * result + channel.hashCode()
        return result
    }

    init {
        this.id = ObjectUtil.checkNotNull(id, "id")
        this.destination = ObjectUtil.checkNotNull(destination, "destination")
        this.channel = ObjectUtil.checkNotNull(channel, "channel")
    }
}