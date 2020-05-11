package com.shengshijie.server.http.config

import com.shengshijie.server.IServer
import io.netty.handler.logging.LogLevel

class ServerConfig(mBuilder: Builder) {

    fun getConfig(): HashMap<String, String> = hashMapOf(
            "debug" to debug.toString(),
            "logLevel" to logLevel.toString(),
            "port" to port.toString(),
            "backlog" to backlog.toString(),
            "corePoolSize" to corePoolSize.toString(),
            "maximumPoolSize" to maximumPoolSize.toString(),
            "workQueueCapacity" to workQueueCapacity.toString(),
            "tcpNodelay" to tcpNodelay.toString(),
            "soKeepalive" to soKeepalive.toString(),
            "soReuseaddr" to soReuseaddr.toString(),
            "soRcvbuf" to soRcvbuf.toString(),
            "soSndbuf" to soSndbuf.toString(),
            "maxContentLength" to maxContentLength.toString(),
            "packageNameList" to packageNameList.toString())

    internal var port = mBuilder.mPort
    internal var debug: Boolean = mBuilder.mDebug
    internal var logLevel: LogLevel = mBuilder.mLogLevel

    internal var backlog: Int = mBuilder.mBacklog

    internal var tcpNodelay: Boolean = mBuilder.mTcpNodelay
    internal var soKeepalive: Boolean = mBuilder.mSoKeepalive
    internal var soReuseaddr: Boolean = mBuilder.mSoReuseaddr
    internal var soRcvbuf: Int = mBuilder.mSoRcvbuf
    internal var soSndbuf: Int = mBuilder.mSoSndbuf
    internal var maxContentLength: Int = mBuilder.mMaxContentLength

    internal var corePoolSize: Int = mBuilder.mCorePoolSize
    internal var maximumPoolSize: Int = mBuilder.mMaximumPoolSize
    internal var workQueueCapacity: Int = mBuilder.mWorkQueueCapacity

    internal var log: (level: LogLevel, content: String) -> Unit = mBuilder.mLog
    internal var packageNameList: MutableList<String> = mBuilder.mPackageNameList
    internal var server: IServer? = mBuilder.mServer

    companion object {
        internal var defaultServerConfig: ServerConfig = Builder().build()
    }

    class Builder {

        internal var mPort: Int = 8888
            private set
        internal var mDebug: Boolean = true
            private set
        internal var mLogLevel: LogLevel = LogLevel.DEBUG
            private set
        internal var mBacklog: Int = Constant.DEFAULT_BACKLOG
            private set
        internal var mTcpNodelay: Boolean = Constant.DEFAULT_TCP_NODELAY
            private set
        internal var mSoKeepalive: Boolean = Constant.DEFAULT_SO_KEEPALIVE
            private set
        internal var mSoReuseaddr: Boolean = Constant.DEFAULT_SO_REUSEADDR
            private set
        internal var mSoRcvbuf: Int = Constant.DEFAULT_SO_RCVBUF
            private set
        internal var mSoSndbuf: Int = Constant.DEFAULT_SO_SNDBUF
            private set
        internal var mMaxContentLength: Int = Constant.DEFAULT_MAX_CONTENT_LENGTH
            private set
        internal var mCorePoolSize: Int = Constant.DEFAULT_CORE_POOL_SIZE
            private set
        internal var mMaximumPoolSize: Int = Constant.DEFAULT_MAXIMUM_POOL_SIZE
            private set
        internal var mWorkQueueCapacity: Int = Constant.DEFAULT_WORK_QUEUE_CAPACITY
            private set
        internal var mLog: (level: LogLevel, content: String) -> Unit = { l, s -> println(s) }
            private set
        internal var mPackageNameList: MutableList<String> = mutableListOf()
            private set
        internal var mServer: IServer? = null
            private set

        fun setPort(port: Int): Builder {
            mPort = port
            return this
        }

        fun setDebug(debug: Boolean): Builder {
            mDebug = debug
            return this
        }

        fun setLogLevel(logLevel: LogLevel): Builder {
            mLogLevel = logLevel
            return this
        }

        fun setBackLog(backlog: Int): Builder {
            mBacklog = backlog
            return this
        }

        fun setCorePoolSize(corePoolSize: Int): Builder {
            mCorePoolSize = corePoolSize
            return this
        }

        fun setMaximumPoolSize(maximumPoolSize: Int): Builder {
            mMaximumPoolSize = maximumPoolSize
            return this
        }

        fun setWorkQueueCapacity(workQueueCapacity: Int): Builder {
            mWorkQueueCapacity = workQueueCapacity
            return this
        }


        fun setTcpNodelay(tcpNodelay: Boolean): Builder {
            mTcpNodelay = tcpNodelay
            return this
        }

        fun setSoKeepalive(soKeepalive: Boolean): Builder {
            mSoKeepalive = soKeepalive
            return this
        }

        fun setSoReuseaddr(soReuseaddr: Boolean): Builder {
            mSoReuseaddr = soReuseaddr
            return this
        }

        fun setSoRcvbuf(soRcvbuf: Int): Builder {
            mSoRcvbuf = soRcvbuf
            return this
        }

        fun setSoSndbuf(soSndbuf: Int): Builder {
            mSoSndbuf = soSndbuf
            return this
        }

        fun setMaxContentLength(maxContentLength: Int): Builder {
            mMaxContentLength = maxContentLength
            return this
        }

        fun setLog(log: (level: LogLevel, content: String) -> Unit): Builder {
            mLog = log
            return this
        }

        fun setPackageNameList(packageNameList: MutableList<String>): Builder {
            mPackageNameList = packageNameList
            return this
        }

        fun setServer(server: IServer?): Builder {
            mServer = server
            return this
        }

        fun build(): ServerConfig {
            return ServerConfig(this)
        }

    }

}