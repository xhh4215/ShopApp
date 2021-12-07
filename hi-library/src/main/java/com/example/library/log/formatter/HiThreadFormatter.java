package com.example.library.log.formatter;

/***
 * @author 栾桂明
 * @desc 线程格式化器
 * @date 2021年12月2日
 */
public class HiThreadFormatter implements HiLogFormatter<Thread> {
    @Override
    public String format(Thread data) {
        return "Thread" + data.getName();
    }
}
