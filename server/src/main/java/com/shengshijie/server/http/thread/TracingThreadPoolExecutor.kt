package com.shengshijie.server.http.thread

import com.shengshijie.server.ServerManager
import com.shengshijie.server.http.thread.NamedThreadFactory
import java.util.concurrent.BlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

internal class TracingThreadPoolExecutor(corePoolSize: Int, maximumPoolSize: Int, workQueue: BlockingQueue<Runnable?>?) : ThreadPoolExecutor(corePoolSize, maximumPoolSize, 0L, TimeUnit.MILLISECONDS, workQueue) {

    private val pendingTasks = AtomicInteger()

    override fun beforeExecute(t: Thread?, r: Runnable?) {
        ServerManager.mStatus.pendingRequestsIncrement()
    }

    override fun afterExecute(r: Runnable?, t: Throwable?) {
        ServerManager.mStatus.pendingRequestsDecrement()
    }

    fun getPendingTasks(): Int {
        return pendingTasks.get()
    }

    init {
        threadFactory = NamedThreadFactory("worker", true)
        ServerManager.mStatus.workerPool(this)
    }

}