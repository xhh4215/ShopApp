package com.example.library.log.printer;


import com.example.library.log.HiLogConfig;

import org.jetbrains.annotations.NotNull;

public interface HiLogPrinter {
    void print(@NotNull HiLogConfig config, int level, String tag, @NotNull String printString);
}
