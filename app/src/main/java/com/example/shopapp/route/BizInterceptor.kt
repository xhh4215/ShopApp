package com.example.shopapp.route

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Interceptor
import com.alibaba.android.arouter.facade.callback.InterceptorCallback
import com.alibaba.android.arouter.facade.template.IInterceptor
import com.example.library.utils.MainHandler
import com.example.shopapp.biz.account.AccountManager
import java.lang.RuntimeException

@Interceptor(priority = 9)
class BizInterceptor : IInterceptor {
    var context: Context? = null
    override fun init(context: Context?) {
        this.context = context;
    }

    override fun process(postcard: Postcard?, callback: InterceptorCallback?) {
        val flag = postcard!!.extra
        if (flag and (RouteFlag.FLAG_LOGIN) != 0) {
            callback!!.onInterrupt(RuntimeException("need login"))
            loginInterceptor(postcard, callback)
        } else {
            callback!!.onContinue(postcard)
        }
    }

    private fun loginInterceptor(postcard: Postcard?, callback: InterceptorCallback?) {
        MainHandler.post(runnable = Runnable {
            Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show()
            if (AccountManager.isLogin()) {
                callback?.onContinue(postcard)
            } else {
                AccountManager.login(context, observer = Observer { success ->
                    callback?.onContinue(postcard)
                })
            }
        })
    }


}