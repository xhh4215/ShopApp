package com.example.shopapp.http.api

import android.os.Bundle
import com.example.library.restful.HiInterceptor
import com.example.library.restful.HiResponse
 import com.example.shopapp.route.HiRoute

class HttpStatusInterceptor : HiInterceptor {
    override fun intercept(chain: HiInterceptor.Chain): Boolean {
        val response = chain.response()
        if (!chain.isRequestPeriod && response != null) {
            when (response.code) {
                HiResponse.RC_NEED_LOGIN -> {
                    HiRoute.startActivity(
                        null,
                        destination = HiRoute.Destination.ACCOUNT_LOGIN
                    )
                }
                HiResponse.RC_AUTH_TOKEN_EXPIRED , (HiResponse.RC_AUTH_TOKEN_INVALID) , (
                        HiResponse.RC_USER_FORBID) -> {
                    var helpUrl: String? = null
                    if (response.errorData != null) {
                        helpUrl = response.errorData!!["helpUrl"]
                    }
                    val bundle = Bundle()
                    bundle.putString("degrade_title", "非法访问")
                    bundle.putString("degrade_desc", response.msg)
                    bundle.putString("degrade_action", helpUrl)
                    HiRoute.startActivity(
                        null,
                        bundle,
                        destination = HiRoute.Destination.DEGRADE_GLOBAL
                    )
                }
            }
        }

        return false
    }
}