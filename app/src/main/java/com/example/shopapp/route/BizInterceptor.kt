package com.example.shopapp.route

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Interceptor
import com.alibaba.android.arouter.facade.callback.InterceptorCallback
import com.alibaba.android.arouter.facade.template.IInterceptor
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
            loginInterceptor()
        } else {
            callback!!.onContinue(postcard)
        }
    }

    private fun loginInterceptor() {
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show()
            HiRoute.startActivity(context, destination = HiRoute.Destination.ACCOUNT_LOGIN)

        }
    }


}