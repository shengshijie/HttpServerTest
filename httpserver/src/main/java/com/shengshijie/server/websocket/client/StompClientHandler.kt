package com.shengshijie.server.websocket.client

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.codec.stomp.DefaultStompFrame
import io.netty.handler.codec.stomp.StompCommand
import io.netty.handler.codec.stomp.StompFrame
import io.netty.handler.codec.stomp.StompHeaders

class StompClientHandler : SimpleChannelInboundHandler<StompFrame>() {
    private enum class ClientState {
        AUTHENTICATING, AUTHENTICATED, SUBSCRIBED, DISCONNECTING
    }

    private var state: ClientState? = null

    @Throws(Exception::class)
    override fun channelActive(ctx: ChannelHandlerContext) {
        state = ClientState.AUTHENTICATING
        val connFrame: StompFrame = DefaultStompFrame(StompCommand.CONNECT)
        connFrame.headers()[StompHeaders.ACCEPT_VERSION] = "1.2"
        connFrame.headers()[StompHeaders.HOST] = StompClient.HOST
        connFrame.headers()[StompHeaders.LOGIN] = StompClient.LOGIN
        connFrame.headers()[StompHeaders.PASSCODE] = StompClient.PASSCODE
        ctx.writeAndFlush(connFrame)
    }

    @Throws(Exception::class)
    override fun channelRead0(ctx: ChannelHandlerContext, frame: StompFrame) {
        val subscrReceiptId = "001"
        val disconReceiptId = "002"
        when (frame.command()) {
            StompCommand.CONNECTED -> {
                val subscribeFrame: StompFrame = DefaultStompFrame(StompCommand.SUBSCRIBE)
                subscribeFrame.headers()[StompHeaders.DESTINATION] = StompClient.TOPIC
                subscribeFrame.headers()[StompHeaders.RECEIPT] = subscrReceiptId
                subscribeFrame.headers()[StompHeaders.ID] = "1"
                println("connected, sending subscribe frame: $subscribeFrame")
                state = ClientState.AUTHENTICATED
                ctx.writeAndFlush(subscribeFrame)
            }
            StompCommand.RECEIPT -> {
                val receiptHeader = frame.headers().getAsString(StompHeaders.RECEIPT_ID)
                if (state == ClientState.AUTHENTICATED && receiptHeader == subscrReceiptId) {
                    val msgFrame: StompFrame = DefaultStompFrame(StompCommand.SEND)
                    msgFrame.headers()[StompHeaders.DESTINATION] = StompClient.TOPIC
                    msgFrame.content().writeBytes("some payload".toByteArray())
                    println("subscribed, sending message frame: $msgFrame")
                    state = ClientState.SUBSCRIBED
                    ctx.writeAndFlush(msgFrame)
                } else if (state == ClientState.DISCONNECTING && receiptHeader == disconReceiptId) {
                    println("disconnected")
                    ctx.close()
                } else {
                    throw IllegalStateException("received: $frame, while internal state is $state")
                }
            }
            StompCommand.MESSAGE -> if (state == ClientState.SUBSCRIBED) {
                println("received frame: $frame")
                val disconnFrame: StompFrame = DefaultStompFrame(StompCommand.DISCONNECT)
                disconnFrame.headers()[StompHeaders.RECEIPT] = disconReceiptId
                println("sending disconnect frame: $disconnFrame")
                state = ClientState.DISCONNECTING
                ctx.writeAndFlush(disconnFrame)
            }
            else -> {
                //
            }
        }
    }

    @Throws(Exception::class)
    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        cause.printStackTrace()
        ctx.close()
    }
}