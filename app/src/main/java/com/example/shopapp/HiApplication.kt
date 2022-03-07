package com.example.shopapp

import com.alibaba.android.arouter.launcher.ARouter
import com.example.common.ui.component.HiBaseApplication
class HiApplication : HiBaseApplication() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            ARouter.openLog()
            ARouter.openDebug()
        }
        //初始化ARouter路由组件
        ARouter.init(this)
    }
}