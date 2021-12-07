package com.example.library.log.formatter;

/***
 * @author 栾桂明
 * @desc 日志的格式化接口
 * @date 2021年12月2日
 */
public interface HiLogFormatter<T> {
    String format(T data);

}
