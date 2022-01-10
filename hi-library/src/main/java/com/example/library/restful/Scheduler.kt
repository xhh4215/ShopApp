package com.example.library.restful

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
            delegate.enqueue(object : HiCallBack<T> {
                override fun onSuccess(response: HiResponse<T>) {
                    dispatchInterceptor(request, response)
                    if (callBack != null) {
                        callBack.onSuccess(response)
                    }
                }

                override fun onFailed(throwable: Throwable) {
                    if (callBack != null) callBack.onFailed(throwable)
                }

            })

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