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

/****
 * @author 栾桂明
 * @desc 通过ARouter实现登录的路由拦截
 */
@Interceptor(priority = 9)
class BizInterceptor : IInterceptor {
    var context: Context? = null

    /***
     * 这里做的是拦截器的初始化的操作
     */
    override fun init(context: Context?) {
        this.context = context;
    }

    /***
     * 拦截器的具体处理逻辑实现的地方
     */
    override fun process(postcard: Postcard?, callback: InterceptorCallback?) {
        //目标页面需要的额外数据
        val flag = postcard!!.extra
        //是否需要登录
        if ((flag and (RouteFlag.FLAG_LOGIN) != 0)) {
            loginInterceptor(postcard, callback)
        } else {
            callback!!.onContinue(postcard)
        }
    }

    /***
     * 登录拦截器
     */
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