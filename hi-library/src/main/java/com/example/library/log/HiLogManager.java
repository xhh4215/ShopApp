package com.example.library.log;


import com.example.library.log.printer.HiLogPrinter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/***
 * @author 栾桂明
 * @desc 日志打印框架的管理类
 * @date 2021年12月2日
 */
public class HiLogManager {
    private HiLogConfig config;
    private static HiLogManager instance;

    private List<HiLogPrinter> printers = new ArrayList<>();

    private HiLogManager(HiLogConfig config, HiLogPrinter[] printers) {

        this.config = config;
        this.printers.addAll(Arrays.asList(printers));
    }

    public static HiLogManager getInstance() {
        return instance;
    }

    public static void init(@NotNull HiLogConfig config, HiLogPrinter... printers) {
        instance = new HiLogManager(config, printers);
    }

    public HiLogConfig getConfig() {
        return config;
    }

    public List<HiLogPrinter> getPrinters() {
        return printers;
    }

    public void removePrinter(HiLogPrinter printer) {
        if (printer != null) {
            printers.remove(printer);
        }
    }

    public void addPrinter(HiLogPrinter printer) {
        printers.add(printer);
    }

}
