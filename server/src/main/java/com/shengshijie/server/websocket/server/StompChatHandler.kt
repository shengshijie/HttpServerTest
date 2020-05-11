package com.shengshijie.server.websocket.server

import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandler.Sharable
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.codec.stomp.DefaultStompFrame
import io.netty.handler.codec.stomp.StompCommand
import io.netty.handler.codec.stomp.StompFrame
import io.netty.handler.codec.stomp.StompHeaders
import io.netty.util.CharsetUtil
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

@Sharable
class StompChatHandler : SimpleChannelInboundHandler<StompFrame>() {
    private val chatDestinations: ConcurrentMap<String?, MutableSet<StompSubscription>> = ConcurrentHashMap()

    @Throws(Exception::class)
    override fun channelRead0(ctx: ChannelHandlerContext, inboundFrame: StompFrame) {
        val decoderResult = inboundFrame.decoderResult()
        if (decoderResult.isFailure) {
            sendErrorFrame("rejected frame", decoderResult.toString(), ctx)
            return
        }
        when (inboundFrame.command()) {
            StompCommand.STOMP, StompCommand.CONNECT -> onConnect(ctx, inboundFrame)
            StompCommand.SUBSCRIBE -> onSubscribe(ctx, inboundFrame)
            StompCommand.SEND -> onSend(ctx, inboundFrame)
            StompCommand.UNSUBSCRIBE -> onUnsubscribe(ctx, inboundFrame)
            StompCommand.DISCONNECT -> onDisconnect(ctx, inboundFrame)
            else -> sendErrorFrame("unsupported command",
                    "Received unsupported command " + inboundFrame.command(), ctx)
        }
    }

    private fun onSubscribe(ctx: ChannelHandlerContext, inboundFrame: StompFrame) {
        val destination = inboundFrame.headers().getAsString(StompHeaders.DESTINATION)
        val subscriptionId = inboundFrame.headers().getAsString(StompHeaders.ID)
        if (destination == null || subscriptionId == null) {
            sendErrorFrame("missed header", "Required 'destination' or 'id' header missed", ctx)
            return
        }
        var subscriptions = chatDestinations[destination]
        if (subscriptions == null) {
            subscriptions = HashSet()
            val previousSubscriptions = chatDestinations.putIfAbsent(destination, subscriptions)
            if (previousSubscriptions != null) {
                subscriptions = previousSubscriptions
            }
        }
        val subscription = StompSubscription(subscriptionId, destination, ctx.channel())
        if (subscriptions.contains(subscription)) {
            sendErrorFrame("duplicate subscription",
                    "Received duplicate subscription id=$subscriptionId", ctx)
            return
        }
        subscriptions.add(subscription)
        ctx.channel().closeFuture().addListener { chatDestinations[subscription.destination()]!!.remove(subscription) }
        val receiptId = inboundFrame.headers().getAsString(StompHeaders.RECEIPT)
        if (receiptId != null) {
            val receiptFrame: StompFrame = DefaultStompFrame(StompCommand.RECEIPT)
            receiptFrame.headers()[StompHeaders.RECEIPT_ID] = receiptId
            ctx.writeAndFlush(receiptFrame)
        }
    }

    private fun onSend(ctx: ChannelHandlerContext, inboundFrame: StompFrame) {
        val destination = inboundFrame.headers().getAsString(StompHeaders.DESTINATION)
        if (destination == null) {
            sendErrorFrame("missed header", "required 'destination' header missed", ctx)
            return
        }
        val subscriptions: Set<StompSubscription> = chatDestinations[destination]!!
        for (subscription in subscriptions) {
            subscription.channel().writeAndFlush(transformToMessage(inboundFrame, subscription))
        }
    }

    private fun onUnsubscribe(ctx: ChannelHandlerContext, inboundFrame: StompFrame) {
        val subscriptionId = inboundFrame.headers().getAsString(StompHeaders.SUBSCRIPTION)
        for ((_, value) in chatDestinations) {
            val iterator = value.iterator()
            while (iterator.hasNext()) {
                val subscription = iterator.next()
                if (subscription.id() == subscriptionId && subscription.channel() == ctx.channel()) {
                    iterator.remove()
                    return
                }
            }
        }
    }

    companion object {
        private fun onConnect(ctx: ChannelHandlerContext, inboundFrame: StompFrame) {
            val acceptVersions = inboundFrame.headers().getAsString(StompHeaders.ACCEPT_VERSION)
            val handshakeAcceptVersion = ctx.channel().attr<StompVersion>(StompVersion.Companion.CHANNEL_ATTRIBUTE_KEY).get()
            if (acceptVersions == null || !acceptVersions.contains(handshakeAcceptVersion.version())) {
                sendErrorFrame("invalid version",
                        "Received invalid version, expected " + handshakeAcceptVersion.version(), ctx)
                return
            }
            val connectedFrame: StompFrame = DefaultStompFrame(StompCommand.CONNECTED)
            connectedFrame.headers()
                    .set(StompHeaders.VERSION, handshakeAcceptVersion.version())
                    .set(StompHeaders.SERVER, "Netty-Server")[StompHeaders.HEART_BEAT] = "0,0"
            ctx.writeAndFlush(connectedFrame)
        }

        private fun onDisconnect(ctx: ChannelHandlerContext, inboundFrame: StompFrame) {
            val receiptId = inboundFrame.headers().getAsString(StompHeaders.RECEIPT)
            if (receiptId == null) {
                ctx.close()
                return
            }
            val receiptFrame: StompFrame = DefaultStompFrame(StompCommand.RECEIPT)
            receiptFrame.headers()[StompHeaders.RECEIPT_ID] = receiptId
            ctx.writeAndFlush(receiptFrame).addListener(ChannelFutureListener.CLOSE)
        }

        private fun sendErrorFrame(message: String, description: String?, ctx: ChannelHandlerContext) {
            val errorFrame: StompFrame = DefaultStompFrame(StompCommand.ERROR)
            errorFrame.headers()[StompHeaders.MESSAGE] = message
            if (description != null) {
                errorFrame.content().writeCharSequence(description, CharsetUtil.UTF_8)
            }
            ctx.writeAndFlush(errorFrame).addListener(ChannelFutureListener.CLOSE)
        }

        private fun transformToMessage(sendFrame: StompFrame, subscription: StompSubscription): StompFrame {
            val messageFrame: StompFrame = DefaultStompFrame(StompCommand.MESSAGE, sendFrame.content().retainedDuplicate())
            val id = UUID.randomUUID().toString()
            messageFrame.headers()
                    .set(StompHeaders.MESSAGE_ID, id)
                    .set(StompHeaders.SUBSCRIPTION, subscription.id())[StompHeaders.CONTENT_LENGTH] = Integer.toString(messageFrame.content().readableBytes())
            val contentType = sendFrame.headers()[StompHeaders.CONTENT_TYPE]
            if (contentType != null) {
                messageFrame.headers()[StompHeaders.CONTENT_TYPE] = contentType
            }
            return messageFrame
        }
    }
}