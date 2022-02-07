package com.example.library.restful

import android.util.Log
import android.widget.Toast
import com.example.library.cache.HiStorage
import com.example.library.executor.HiExecutor
import com.example.library.restful.annotation.CacheStrategy
import com.example.library.utils.AppGlobals
import com.example.library.utils.MainHandler

/***
 * 代理call实现拦截器的派发
 */
class Scheduler(
    private val callFactory: HiCall.Factory,
    private val interceptors: MutableList<HiInterceptor>
) {
    fun newCall(request: HiRequest): HiCall<*> {
        //通过callFactory创建call对象
        val realCall = callFactory.newCall(request)
        //返回的是一个代理了call操作的ProxyCall
        return ProxyCall(realCall, request)
    }

    internal inner class ProxyCall<T>(
        private val delegate: HiCall<T>,
        private val request: HiRequest
    ) : HiCall<T> {
        /***
         * 同步请求
         */
        override fun execute(): HiResponse<T> {
            dispatchInterceptor(request, null)
            val response = delegate.execute()
            dispatchInterceptor(request, response)
            return response
        }

        /***
         * 异步请求
         */
        override fun enqueue(callBack: HiCallBack<T>) {
            dispatchInterceptor(request, null)
            if (request.cacheStrategy == CacheStrategy.CACHE_FIRST) {
                HiExecutor.execute(runnable = Runnable {
                    val cacheResponse = readCache<T>()
                    if (cacheResponse.data != null) {
                        MainHandler.sendAtFrontOfQueue(runnable = Runnable {
                            callBack.onSuccess(cacheResponse)
                        })
                    }
                })
            }
            delegate.enqueue(object : HiCallBack<T> {
                override fun onSuccess(response: HiResponse<T>) {
                    dispatchInterceptor(request, response)
                    saveCacheIfNeed(response)
                    callBack.onSuccess(response)
                }

                override fun onFailed(throwable: Throwable) {
                    callBack.onFailed(throwable)
                }

            })

        }

        private fun saveCacheIfNeed(response: HiResponse<T>) {
            if (request.cacheStrategy == CacheStrategy.CACHE_FIRST
                || request.cacheStrategy == CacheStrategy.NET_CACHE
            ) {
                if (response.data != null) {
                    HiExecutor.execute(runnable = Runnable {
                         HiStorage.saveCache(request.getCacheKey(), response.data)
                    })
                }
            }
        }


        private fun <T> readCache(): HiResponse<T> {
            val cacheKey = request.getCacheKey()
            Log.e("read_http_request", cacheKey)
            val cache = HiStorage.getCache<T>(cacheKey)
            val cacheResponse = HiResponse<T>()
            cacheResponse.data = cache
            cacheResponse.code = HiResponse.CACHE_SUCCESS
            cacheResponse.msg = "缓存获取成功"
            return cacheResponse
        }

        /***
         * 拦截器的分发
         */
        private fun dispatchInterceptor(request: HiRequest, response: HiResponse<T>?) {
            InterceptorChain(request, response).dispatch()
        }


        internal inner class InterceptorChain(
            private val request: HiRequest,
            private val response: HiResponse<T>?
        ) : HiInterceptor.Chain {
            //分发的第几个拦截器
            private var callIndex = 0

            override val isRequestPeriod: Boolean
                get() = response == null

            override fun request(): HiRequest {
                return request
            }

            override fun response(): HiResponse<*>? {
                return response
            }

            fun dispatch() {
                val interceptor = interceptors[callIndex]
                //返回数值表示是否拦截
                val intercept = interceptor.intercept(this)
                callIndex++
                if (!intercept && callIndex < interceptors.size) {
                    dispatch()
                }
            }

        }


    }


}