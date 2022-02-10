package com.example.common.http

import com.example.library.log.HiLog
import com.example.library.restful.HiInterceptor
import com.example.library.restful.HiRequest
import com.example.service_login.LoginServiceProvider

/***
 * @author 栾桂明
 * @desc 网络请求的拦截器
 * @date 2022年1月3日
 */
class BizInterceptor : HiInterceptor {
    override fun intercept(chain: HiInterceptor.Chain): Boolean {
        val request = chain.request()
        val response = chain.response()
        if (chain.isRequestPeriod) {
            val boardingPass = LoginServiceProvider.getBoardingPass() ?: ""
            request.addHeader("boarding-pass", boardingPass)
            request.addHeader("auth-token", "MTU5Mjg1MDg3NDcwNw11.26==")
        } else if (response != null) {
            var outputBuilder = StringBuilder()
            val httpMethod =
                if (request.httpMethod == HiRequest.METHOD.GET) "GET" else "POST"

            val requestUrl = request.endPointUrl()
            outputBuilder.append("\n$requestUrl==>$httpMethod\n")
            if (request.headers != null) {
                outputBuilder.append("【headers\n")
                request.headers!!.forEach(action = {
                    outputBuilder.append(it.key + ":" + it.value)
                    outputBuilder.append("\n")
                })
                outputBuilder.append("headers】\n")
            }
            if (request.parameters != null && request.parameters!!.isNotEmpty()) {
                outputBuilder.append("【parameters==>\n")
                request.parameters!!.forEach(action = {
                    outputBuilder.append(it.key + ":" + it.value + "\n")
                })
                outputBuilder.append("parameters】\n")
            }

            outputBuilder.append("【response==>\n")
            outputBuilder.append(response.rawData + "\n")
            outputBuilder.append("response】\n")
//            HiLog.dt("BizInterceptor Http:", outputBuilder.toString())

        }
        return false
    }
}