package com.example.shopapp.http

import android.util.Log
import com.example.library.restful.HiInterceptor
import com.example.shopapp.biz.account.AccountManager

/***
 * @author 栾桂明
 * @desc 网络请求的拦截器
 * @date 2022年1月3日
 */
class BizInterceptor : HiInterceptor {
    override fun intercept(chain: HiInterceptor.Chain): Boolean {
        if (chain.isRequestPeriod) {
            val request = chain.request()
            request.addHeader("boarding-pass", AccountManager.getBoardingPass() ?: "")
            request.addHeader("auth-token", "MTU5Mjg1MDg3NDcwNw11.26==")
        } else if (chain.response() != null) {
            Log.e("BizInterceptor", chain.request()!!.endPointUrl())
            chain.response()!!.rawData?.let { Log.e("BizInterceptor", it) }

        }
        return false
    }
}