package com.shengshijie.server.http.thread

import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicInteger

internal class NamedThreadFactory @JvmOverloads constructor(prefix: String = "pool-" + seq.getAndIncrement(), daemon: Boolean = false) : ThreadFactory {

    private val mThreadNum = AtomicInteger(1)
    private val mPrefix: String = "$prefix-thread-"
    private val mDaemon: Boolean = daemon
    private val threadGroup: ThreadGroup?

    override fun newThread(runnable: Runnable): Thread {
        val name = mPrefix + mThreadNum.getAndIncrement()
        val ret = Thread(threadGroup, runnable, name, 0)
        ret.isDaemon = mDaemon
        return ret
    }

    companion object {
        private val seq = AtomicInteger(1)
    }

    init {
        val s = System.getSecurityManager()
        threadGroup = if (s == null) Thread.currentThread().threadGroup else s.threadGroup
    }

}