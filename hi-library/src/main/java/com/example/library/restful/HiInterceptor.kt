package com.example.library.restful

/****
 * 这个拦截器在请求发起以前和发起之后都会派发一次
 */
interface HiInterceptor {
    /***
     * 返回值表示是否拦截本次分发
     */
    fun intercept(chain: Chain): Boolean

    /***
     * Chain 会在派发拦截器的时候创建
     */
    interface Chain {
        /***
         * 是否是请求期间
         */
        val isRequestPeriod: Boolean get() = false

        fun request(): HiRequest

        //在网络发起之前是为空的
        fun response(): HiResponse<*>?
    }

}