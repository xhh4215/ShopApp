package com.example.library.log.printer;



import static com.example.library.log.HiLogConfig.MAX_IEN;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.library.log.HiLogConfig;


public class HiConsolePrinter implements HiLogPrinter {
    @Override
    public void print(@NonNull HiLogConfig config, int level, String tag, @NonNull String printString) {
        int len = printString.length();
        int countOfSub = len / MAX_IEN;
        if (countOfSub > 0) {
            int index = 0;
            for (int i = 0; i < countOfSub; i++) {
                Log.println(level, tag, printString.substring(index, index + MAX_IEN));
                index += MAX_IEN;
            }
            if (index != len) {
                Log.println(level, tag, printString.substring(index, len));
            }
        } else {
            Log.println(level, tag, printString);
        }
    }
}
