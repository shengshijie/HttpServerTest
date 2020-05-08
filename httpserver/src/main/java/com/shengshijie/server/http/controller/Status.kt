package com.shengshijie.server.http.controller

import com.shengshijie.server.http.TracingThreadPoolExecutor
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong


object Status {

    fun getStatus(): HashMap<String, String> = hashMapOf(
            "connections" to getConnections().toString(),
            "totalRequests" to getTotalRequests().toString(),
            "handledRequests" to getHandledRequests().toString(),
            "pendingRequests" to getPendingRequests().toString(),
            "workerPoolSize" to getWorkerPoolSize().toString(),
            "activePoolSize" to getActivePoolSize().toString())

    private val connections = AtomicInteger(0)
    private val pendingRequests = AtomicInteger(0)
    private val totalRequests = AtomicLong(0)
    private val handledRequests = AtomicLong(0)
    private var workerPoolSize = 0
    private var activePoolSize = 0

    private var workerPool: TracingThreadPoolExecutor? = null

    fun totalRequestsIncrement(): Status? {
        totalRequests.incrementAndGet()
        return this
    }

    fun handledRequestsIncrement(): Status? {
        handledRequests.incrementAndGet()
        return this
    }

    fun connectionIncrement(): Status? {
        connections.incrementAndGet()
        return this
    }

    fun connectionDecrement(): Status? {
        connections.decrementAndGet()
        return this
    }

    fun pendingRequestsIncrement(): Status? {
        pendingRequests.incrementAndGet()
        return this
    }

    fun pendingRequestsDecrement(): Status? {
        pendingRequests.decrementAndGet()
        return this
    }

    private fun getConnections(): Int {
        return connections.get()
    }

    private fun getPendingRequests(): Int {
        return pendingRequests.get()
    }

    private fun getTotalRequests(): Long {
        return totalRequests.get()
    }

    private fun getHandledRequests(): Long {
        return handledRequests.get()
    }

    private fun getWorkerPoolSize(): Int {
        workerPoolSize = workerPool!!.poolSize
        return workerPoolSize
    }

    private fun getActivePoolSize(): Int {
        activePoolSize = workerPool!!.activeCount
        return activePoolSize
    }

    fun workerPool(workerPool: TracingThreadPoolExecutor?) {
        this.workerPool = workerPool
    }

}