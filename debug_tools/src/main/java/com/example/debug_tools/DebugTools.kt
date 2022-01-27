package com.example.debug_tools

import android.content.Intent
import android.os.Process
import com.example.common.utils.SPUtil
import com.example.library.utils.AppGlobals

class DebugTools {

    fun buildTime(): String {
        return "构建时间：" + BuildConfig.BUILD_TIME
    }

    fun buildVersion(): String {
        return "构建版本：" + BuildConfig.BUILD_VERSION
    }

    @HiDebug(name = "一键开启Https降级", desc = "将继承Http,可以使用抓包工具明文抓包")
    fun degrade2Http() {
        SPUtil.putBoolean("degrade_http", true)
        val context = AppGlobals.get()?.applicationContext ?: return
        val intent =
            context.packageManager.getLaunchIntentForPackage(context.packageName)
        //得到了 启动页的 intent
        intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
        //杀掉当前进程,并主动启动新的 启动页，以完成重启的动作
        Process.killProcess(Process.myPid())

    }

}