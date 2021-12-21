package com.example.library.executor

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.IntRange
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.ReentrantLock

/***
 * 支持按任务的优先级执行
 * 支持线程池的暂停和恢复
 * 支持异步结果回调主线程
 */
object HiExecutor {
    private const val TAG = "HiExecutor"

    //线程池对象
    private var hiExecutor: ThreadPoolExecutor

    //实现线程池的恢复和暂停
    private var lock: ReentrantLock = ReentrantLock()

    //线程池是不是暂停了
    private var isPause = false
    private var pauseCondition: Condition = lock.newCondition()

    //post数据到主线程
    private val mainHandler = Handler(Looper.getMainLooper())

    init {
        val cpuCount = Runtime.getRuntime().availableProcessors()
        //核心线程数
        val corePoolSize = cpuCount + 1
        //最大线程数
        val maxPoolSize = cpuCount * 2 + 1
        //任务对象
        val blockQueue: PriorityBlockingQueue<out Runnable> = PriorityBlockingQueue()
        //非核心任务的存活时间
        val keepAliveTime = 30L
        val unit = TimeUnit.SECONDS
        val seq = AtomicLong()
        //创建thread的工厂类
        val threadFactory = ThreadFactory {
            val thread = Thread(it)
            thread.name = "hi-executor-" + seq.getAndIncrement()
            return@ThreadFactory thread
        }
        hiExecutor = object : ThreadPoolExecutor(
            corePoolSize,
            maxPoolSize,
            keepAliveTime,
            unit,
            blockQueue as BlockingQueue<Runnable>,
            threadFactory
        ) {
            //在内部的run方法执行之前调用
            override fun beforeExecute(t: Thread?, r: Runnable?) {
                if (isPause) {
                    try {
                        lock.lock()
                        pauseCondition.await()
                    } finally {
                        lock.unlock()
                    }
                }
            }

            //在内部的run方法执行之后调用
            override fun afterExecute(r: Runnable?, t: Throwable?) {
                super.afterExecute(r, t)
                Log.e(TAG, "以执行完的任务的优先级是：" + (r as PriorityRunnable).priority)
            }
        }
    }

    @JvmOverloads
    fun execute(@IntRange(from = 0, to = 10) priority: Int = 0, runnable: Runnable) {
        hiExecutor.execute(PriorityRunnable(priority, runnable))

    }

    @JvmOverloads
    fun execute(@IntRange(from = 0, to = 10) priority: Int = 0, runnable: Callable<*>) {
        hiExecutor.execute(PriorityRunnable(priority, runnable))

    }

    /****
     * 实现compare接口 支持线程对优先级的比较
     */
    class PriorityRunnable(val priority: Int, private val runnable: Runnable) : Runnable,
        Comparable<PriorityRunnable> {
        override fun run() {
            runnable.run()
        }

        override fun compareTo(other: PriorityRunnable): Int {
            return if (this.priority < other.priority) 1 else if (this.priority > other.priority) -1 else 0
        }

    }

    /***
     * 包装Runnable对执行之前和之后和执行中的逻辑处理
     */
    abstract class Callable<T> : Runnable {
        override fun run() {

            mainHandler.post { onPrepare() }

            val t = onBackground()

            mainHandler.post { onComplete(t) }

        }

        open fun onPrepare() {

        }

        abstract fun onBackground(): T


        abstract fun onComplete(t: T)
    }

    @Synchronized
    fun pause() {
        isPause = true
        Log.e(TAG, "hi-executor is pause")
    }

    @Synchronized
    fun resume() {
        isPause = false
        lock.lock()
        try {
            pauseCondition.signalAll()
        } finally {
            lock.unlock()
        }
        Log.e(TAG, "hi-executor is resume")

    }
}