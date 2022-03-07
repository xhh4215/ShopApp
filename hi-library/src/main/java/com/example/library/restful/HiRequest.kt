package com.example.library.restful

import android.text.TextUtils
import androidx.annotation.IntDef
import com.example.library.restful.annotation.CacheStrategy
import java.lang.Exception
import java.lang.IllegalStateException
import java.lang.StringBuilder
import java.lang.reflect.Type
import java.net.URLEncoder

/***
 * @author 栾桂明
 * @desc http请求的request的封装
 * @date 2021年12月22日
 */
open class HiRequest {
    private var cacheStrategyKey: String = ""
    var cacheStrategy: Int = CacheStrategy.NET_ONLY

    //请求类型
    @METHOD
    var httpMethod: Int = 0

    //请求的headers
    var headers: MutableMap<String, String>? = null

    //请求的参数
    var parameters: MutableMap<String, String>? = null

    //域名
    var domainUrl: String? = null

    //相对路径
    var relativeUrl: String? = null

    //返回值的数据类型
    var returnType: Type? = null

    //是否是表单提交
    var formPost: Boolean = true

    @IntDef(value = [METHOD.GET, METHOD.POST, METHOD.DELETE, METHOD.PUT])
    annotation class METHOD {
        companion object {
            const val GET = 0
            const val POST = 1
            const val PUT = 2
            const val DELETE = 3
        }
    }

    fun endPointUrl(): String {
        if (relativeUrl == null) {
            throw  IllegalStateException("relative url must be not null")
        }
        if (relativeUrl!!.startsWith("/")) {
            return domainUrl + relativeUrl
        }
        val indexOf = domainUrl!!.indexOf("/")
        return domainUrl!!.substring(0, indexOf) + relativeUrl
    }

    fun addHeader(name: String, value: String) {
        if (headers == null) {
            headers = mutableMapOf()
        }
        headers!![name] = value
    }

    fun getCacheKey(): String {
        if (!TextUtils.isEmpty(cacheStrategyKey)) {
            return cacheStrategyKey
        }
        val builder = StringBuilder()
        val endUrl = endPointUrl()
        builder.append(endUrl)
        if (endUrl.indexOf("?") > 0 || endUrl.indexOf("&") > 0) {
            builder.append("&")
        } else {
            builder.append("?")
        }
        if (parameters != null) {
            for ((key, value) in parameters!!) {
                try {
                    val encodeValue = URLEncoder.encode(value, "UTF-8")
                    builder.append(key).append("=").append(encodeValue).append("&")
                } catch (e: Exception) {
                    //ignore
                }
            }
            builder.deleteCharAt(builder.length - 1)
            cacheStrategyKey = builder.toString()
        } else {
            cacheStrategyKey = endUrl
        }

        return cacheStrategyKey
    }

}