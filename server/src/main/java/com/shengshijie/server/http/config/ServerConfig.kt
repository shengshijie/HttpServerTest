package com.shengshijie.server.http.config

import com.shengshijie.server.IServer
import com.shengshijie.server.http.serialize.Serialize
import com.shengshijie.server.log.LogLevel

class ServerConfig(mBuilder: Builder) {

    fun getConfig(): HashMap<String, String> = hashMapOf(
            "enableSSL" to enableSSL.toString(),
            "enableCors" to enableCors.toString(),
            "sign" to sign.toString(),
            "rootPath" to rootPath,
            "salt" to salt,
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
            "packageNameList" to packageNameList.toString(),
            "successCode" to successCode.toString(),
            "errorCode" to errorCode.toString())

    internal var enableSSL = mBuilder.mEnableSSL
    internal var enableCors = mBuilder.mEnableCors
    internal var sign = mBuilder.mSign
    internal var rootPath = mBuilder.mRootPath
    internal var salt = mBuilder.mSalt
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
    internal var server: IServer = mBuilder.mServer
    internal var serialize: Serialize = mBuilder.mSerialize
    internal var successCode: Int = mBuilder.mSuccessCode
    internal var errorCode: Int = mBuilder.mErrorCode

    companion object {
        internal var defaultServerConfig: ServerConfig = Builder().build()
    }

    class Builder {

        internal var mEnableSSL = DefaultConfig.DEFAULT_ENABLE_SSL
            private set
        internal var mEnableCors = DefaultConfig.DEFAULT_ENABLE_CORS
            private set
        internal var mRootPath = DefaultConfig.DEFAULT_ROOT_PATH
            private set
        internal var mSign = DefaultConfig.DEFAULT_ENABLE_SIGN
            private set
        internal var mSalt = DefaultConfig.DEFAULT_SALT
            private set
        internal var mPort: Int = DefaultConfig.DEFAULT_PORT
            private set
        internal var mDebug: Boolean = DefaultConfig.DEFAULT_DEBUG
            private set
        internal var mLogLevel: LogLevel = DefaultConfig.DEFAULT_LOG_LEVEL
            private set
        internal var mBacklog: Int = DefaultConfig.DEFAULT_BACKLOG
            private set
        internal var mTcpNodelay: Boolean = DefaultConfig.DEFAULT_TCP_NODELAY
            private set
        internal var mSoKeepalive: Boolean = DefaultConfig.DEFAULT_SO_KEEPALIVE
            private set
        internal var mSoReuseaddr: Boolean = DefaultConfig.DEFAULT_SO_REUSEADDR
            private set
        internal var mSoRcvbuf: Int = DefaultConfig.DEFAULT_SO_RCVBUF
            private set
        internal var mSoSndbuf: Int = DefaultConfig.DEFAULT_SO_SNDBUF
            private set
        internal var mMaxContentLength: Int = DefaultConfig.DEFAULT_MAX_CONTENT_LENGTH
            private set
        internal var mCorePoolSize: Int = DefaultConfig.DEFAULT_CORE_POOL_SIZE
            private set
        internal var mMaximumPoolSize: Int = DefaultConfig.DEFAULT_MAXIMUM_POOL_SIZE
            private set
        internal var mWorkQueueCapacity: Int = DefaultConfig.DEFAULT_WORK_QUEUE_CAPACITY
            private set
        internal var mLog: (level: LogLevel, content: String) -> Unit = DefaultConfig.DEFAULT_DEFAULT_LOG
            private set
        internal var mPackageNameList: MutableList<String> = DefaultConfig.DEFAULT_PACKAGE_LIST
            private set
        internal var mServer: IServer = DefaultConfig.DEFAULT_SERVER
            private set
        internal var mSerialize: Serialize = DefaultConfig.DEFAULT_SERIALIZER
            private set
        internal var mSuccessCode: Int = DefaultConfig.DEFAULT_SUCCESS_CODE
            private set
        internal var mErrorCode: Int = DefaultConfig.DEFAULT_ERROR_CODE_BUSINESS
            private set

        fun setEnableSSL(enableSSL: Boolean): Builder {
            mEnableSSL = enableSSL
            return this
        }

        fun setEnableCors(enableCors: Boolean): Builder {
            mEnableCors = enableCors
            return this
        }

        fun setRootPath(rootPath: String): Builder {
            mRootPath = rootPath
            return this
        }

        fun setSalt(salt: String): Builder {
            mSalt = salt
            return this
        }

        fun setSign(sign: Boolean): Builder {
            mSign = sign
            return this
        }

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

        fun setServer(server: IServer): Builder {
            mServer = server
            return this
        }

        fun setSerialize(serialize: Serialize): Builder {
            mSerialize = serialize
            return this
        }

        fun setSuccessCode(successCode: Int): Builder {
            mSuccessCode = successCode
            return this
        }

        fun setErrorCode(errorCode: Int): Builder {
            mErrorCode = errorCode
            return this
        }

        fun build(): ServerConfig {
            return ServerConfig(this)
        }

    }

}