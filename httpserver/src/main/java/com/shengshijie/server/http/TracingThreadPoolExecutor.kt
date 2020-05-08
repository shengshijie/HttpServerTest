package com.shengshijie.server.http

import com.shengshijie.server.http.controller.Status
import java.util.concurrent.BlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

class TracingThreadPoolExecutor(corePoolSize: Int, maximumPoolSize: Int, workQueue: BlockingQueue<Runnable?>?) : ThreadPoolExecutor(corePoolSize, maximumPoolSize, 0L, TimeUnit.MILLISECONDS, workQueue) {

    private val pendingTasks = AtomicInteger()

    override fun beforeExecute(t: Thread?, r: Runnable?) {
        Status.pendingRequestsIncrement()
    }

    override fun afterExecute(r: Runnable?, t: Throwable?) {
        Status.pendingRequestsDecrement()
    }

    fun getPendingTasks(): Int {
        return pendingTasks.get()
    }

    init {
        threadFactory = NamedThreadFactory("worker", true)
        Status.workerPool(this)
    }

}