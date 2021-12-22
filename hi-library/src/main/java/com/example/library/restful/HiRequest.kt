package com.example.library.restful

import androidx.annotation.IntDef
import java.lang.reflect.Type

/***
 * @author 栾桂明
 * @desc http请求的request的封装
 * @date 2021年12月22日
 */
open class HiRequest {
    //请求类型
    @METHOD
    var httpMethod: Int = 0

    //请求的headers
    var headers: MutableMap<String, String>? = null

    //请求的参数
    var parameters: MutableMap<String, Any>? = null

    //域名
    var domainUrl: String? = null

    //相对路径
    var relativeUrl: String? = null

    //返回值的数据类型
    var returnType: Type? = null

    @IntDef(value = [METHOD.GET, METHOD.POST])
    internal annotation class METHOD {
        companion object {
            const val GET = 0
            const val POST = 1
        }
    }
}