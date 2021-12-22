package com.example.library.restful

/***
 * @author 栾桂明
 * @desc 对接口请求结果的回调处理
 */
interface HiCallBack<T> {
    /***
     * 请求成功的回调
     */
    fun onSuccess(response: HiResponse<T>)

    /***
     * 请求失败的回调
     */
    fun onFiled(throwable: Throwable)
}