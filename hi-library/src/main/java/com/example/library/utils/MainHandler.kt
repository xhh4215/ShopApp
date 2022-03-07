package com.example.library.utils

import android.os.Handler
import android.os.Looper
import android.os.Message

/***
 * @author 栾桂明
 * @desc 一个向主线程发送数据的handler工具类
 */
object MainHandler {
    //通过getMainLooper()获取一个主线程的Looper
    private val handler = Handler(Looper.getMainLooper())

    /***
     * post数据到主线程
     */
    fun post(runnable: Runnable) {
        handler.post(runnable)
    }

    /***
     * 延迟指定时间之后发送数据到主线程
     */
    fun postDelay(delayMills: Long, runnable: Runnable) {
        handler.postDelayed(runnable, delayMills)
    }

    fun sendAtFrontOfQueue(runnable: Runnable) {
        val msg = Message.obtain(handler, runnable)
        handler.sendMessageAtFrontOfQueue(msg)
    }

    /****
     * 从消息队列中移除
     */
    fun remove(runnable: Runnable) {
        handler.removeCallbacks(runnable)
    }
}