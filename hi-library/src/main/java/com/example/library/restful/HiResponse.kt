package com.example.library.restful

/***
 * @author 栾桂明
 * @desc 对http请求的结果的封装
 * @date 2021年12月22日
 */
open class HiResponse<T> {
    companion object {
        val SUCCESS = 0
    }

    //原始数据
    var rawData: String? = null

    //业务状态码
    var code = 0

    //业务数据
    var data: T? = null

    //业务的错误数据
    var errorData: Map<String, String>? = null

    //错误信息
    var msg: String? = null;
}