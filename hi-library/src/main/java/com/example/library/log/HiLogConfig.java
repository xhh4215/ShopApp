package com.example.library.log;

import com.example.library.log.formatter.HiStackTraceFormatter;
import com.example.library.log.formatter.HiThreadFormatter;
import com.example.library.log.printer.HiLogPrinter;

public abstract class HiLogConfig {

    public static int MAX_IEN = 512;
    static HiStackTraceFormatter hiStackTraceFormatter = new HiStackTraceFormatter();
    static HiThreadFormatter hiThreadFormatter = new HiThreadFormatter();

    public JsonParse injectJsonParse() {
        return null;
    }

    public String getGlobalTag() {
        return "HiLog";
    }

    public boolean enable() {
        return true;
    }

    public boolean includeThread() {
        return false;
    }

    public int stackTraceDepth() {
        return 5;
    }

    public HiLogPrinter printers() {
        return null;
    }

    public interface JsonParse {
        String toJson(Object o);
    }
}
