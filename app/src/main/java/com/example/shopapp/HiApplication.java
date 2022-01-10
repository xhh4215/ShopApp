package com.example.shopapp;

import com.alibaba.android.arouter.launcher.ARouter;
import com.example.common.ui.component.HiBaseApplication;
import com.example.library.log.HiLogConfig;
import com.example.library.log.HiLogManager;
import com.example.library.log.printer.HiConsolePrinter;

public class HiApplication extends HiBaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            ARouter.openLog();
            ARouter.openDebug();
        }

        ARouter.init(this);
    }
}
