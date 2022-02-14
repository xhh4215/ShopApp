package com.example.shopapp.degarde

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Interceptor
import com.alibaba.android.arouter.facade.callback.InterceptorCallback
import com.alibaba.android.arouter.facade.template.IInterceptor
import com.example.library.utils.MainHandler
import com.example.biz_login.AccountManager
import com.example.common.route.RouteFlag
import com.example.service_login.LoginServiceProvider
import java.lang.RuntimeException

@Interceptor(priority = 9)
class BizInterceptor : IInterceptor {
    var context: Context? = null
    override fun init(context: Context?) {
        this.context = context;
    }

    override fun process(postcard: Postcard?, callback: InterceptorCallback?) {
        val flag = postcard!!.extra
        if ((flag and (RouteFlag.FLAG_LOGIN) != 0)) {
            loginInterceptor(postcard, callback)
        } else {
            callback!!.onContinue(postcard)
        }
    }

    private fun loginInterceptor(postcard: Postcard?, callback: InterceptorCallback?) {
        MainHandler.post(runnable = Runnable {
            if (AccountManager.isLogin()) {
                callback?.onContinue(postcard)
            } else {
                Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show()
                val observer = Observer<Boolean> {
                    callback?.onContinue(postcard)
                }
                LoginServiceProvider.login(context, observer)
            }
        })
    }


}