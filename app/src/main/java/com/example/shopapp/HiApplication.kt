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

        ARouter.init(this)
    }
}