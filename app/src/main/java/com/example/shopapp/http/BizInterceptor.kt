package com.example.shopapp.http

import android.util.Log
import com.example.common.utils.SPUtil
import com.example.library.restful.HiInterceptor

/***
 * @author 栾桂明
 * @desc 网络请求的拦截器
 * @date 2022年1月3日
 */
class BizInterceptor : HiInterceptor {
    override fun intercept(chain: HiInterceptor.Chain): Boolean {
        if (chain.isRequestPeriod) {
            val request = chain.request()
            val boarding = SPUtil.getString("boarding-pass") ?: ""
            request.addHeader("boarding-pass", boarding)
            request.addHeader("auth-token", "MTU5Mjg1MDg3NDcwNw11.26==")
         } else if (chain.response() != null) {
            Log.e("BizInterceptor", chain.request()!!.endPointUrl())
            Log.e("BizInterceptor", chain.response()!!.rawData)
//            HiLog.dt("BizInterceptor", chain.request()!!.endPointUrl())
//            HiLog.dt("BizInterceptor", chain.response()!!.rawData)

        }
        return false
    }
}