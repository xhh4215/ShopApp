package com.example.library.restful

import java.io.IOException
import kotlin.jvm.Throws

/****
 * @author 栾桂明
 * @desc 这是一个抽象的Call不同的网络接口有不同的实现 以自己的
 * 方式方法请求
 * @date 2021年12月22日
 */
interface HiCall<T> {
    /***
     * 同步请求
     */
    @Throws(IOException::class)
    fun execute(): HiResponse<T>

    /***
     * 异步请求
     */
    fun enqueue(callBack: HiCallBack<T>)

    /****
     * 不同的网路框架 通过这个factory实现自己的创建call的逻辑
     */
    interface Factory {
        fun newCall(request: HiRequest): HiCall<*>
    }
}