package com.example.library.log;

import android.util.Log;


import com.example.library.log.printer.HiLogPrinter;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

/***
 * @author 栾桂明
 * @desc 日志打印框架的门面
 * @date 2021年12月2日
 */
public class HiLog {
    private static final String HI_LOG_PACKAGE;

    static {
        String className = HiLog.class.getName();
        HI_LOG_PACKAGE = className.substring(0, className.lastIndexOf(".") + 1);
    }

    public static void v(Object... contents) {
        log(HiLogType.V, contents);
    }


    public static void vt(String tag, Object... contents) {
        log(HiLogType.V, tag, contents);
    }


    public static void d(Object... contents) {
        log(HiLogType.D, contents);
    }

    public static void dt(String tag, Object... contents) {
        log(HiLogType.D, tag, contents);
    }

    public static void i(Object... contents) {
        log(HiLogType.I, contents);
    }

    public static void it(String tag, Object... contents) {
        log(HiLogType.I, tag, contents);
    }

    public static void w(Object... contents) {
        log(HiLogType.W, contents);
    }

    public static void wt(String tag, Object... contents) {
        log(HiLogType.W, tag, contents);
    }

    public static void e(Object... contents) {
        log(HiLogType.E, contents);
    }

    public static void et(String tag, Object... contents) {
        log(HiLogType.E, tag, contents);
    }

    public static void a(Object... contents) {
        log(HiLogType.A, contents);
    }

    public static void at(String tag, Object... contents) {
        log(HiLogType.A, tag, contents);
    }

    public static void log(@HiLogType.TYPE int type, Object... contents) {
        log(type, HiLogManager.getInstance().getConfig().getGlobalTag(), contents);
    }

    public static void log(@HiLogType.TYPE int type, @NotNull String tag, Object... contents) {
        log(HiLogManager.getInstance().getConfig(), type, tag, contents);
    }

    public static void log(@NotNull HiLogConfig config, @HiLogType.TYPE int type, @NotNull String tag, Object... contents) {
        if (!config.enable()) {
            return;
        }
        StringBuilder builder = new StringBuilder();
        if (config.includeThread()) {
            String threadInfo = HiLogConfig.hiThreadFormatter.format(Thread.currentThread());
            builder.append(threadInfo).append("\n");
        }
        if (config.stackTraceDepth() > 0) {
            String staceTraceInfo = HiLogConfig.hiStackTraceFormatter.format(HiStackTraceUtil.getCroppedRealStackTrace(new Throwable().getStackTrace(), HI_LOG_PACKAGE, config.stackTraceDepth()));
            builder.append(staceTraceInfo).append("\n");
        }
        List<HiLogPrinter> printers = config.printers() != null ? Arrays.asList(config.printers()) : HiLogManager.getInstance().getPrinters();
        if (printers == null) {
            return;
        }

        String body = parseBody(contents, config);
        builder.append(body);
        for (HiLogPrinter printer : printers) {
            printer.print(config, type, tag, builder.toString());
        }
        Log.println(type, tag, body);
    }

    private static String parseBody(@NotNull Object[] contents, @NotNull HiLogConfig config) {
        if (config.injectJsonParse() != null) {
            return config.injectJsonParse().toJson(contents);
        }
        StringBuilder builder = new StringBuilder();
        for (Object o : contents) {
            builder.append(o.toString()).append(";");
        }
        if (builder.length() > 0) {
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }
}
